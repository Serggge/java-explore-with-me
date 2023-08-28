package ru.practicum.explorewithme.reaction.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventMapper;
import ru.practicum.explorewithme.exception.illegal.EventStateException;
import ru.practicum.explorewithme.exception.illegal.ReactionStateException;
import ru.practicum.explorewithme.exception.notFound.CategoryNotFoundException;
import ru.practicum.explorewithme.exception.notFound.EventNotFoundException;
import ru.practicum.explorewithme.exception.notFound.ReactionNotFoundException;
import ru.practicum.explorewithme.exception.notFound.UserNotFoundException;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.repository.view.LikesView;
import ru.practicum.explorewithme.reaction.repository.ReactionRepository;
import ru.practicum.explorewithme.reaction.repository.view.Reactions;
import ru.practicum.explorewithme.reaction.service.ReactionMapper;
import ru.practicum.explorewithme.reaction.service.ReactionService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Setter
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final CategoryRepository categoryRepository;
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReactionMapper reactionMapper;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto add(long eventId, long userId, boolean positive) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id=%d was not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventStateException("Cant react on unpublished event id=" + eventId);
        }
        Reaction reaction;
        Optional<Reaction> optReaction = reactionRepository.findByEventIdAndUserId(eventId, userId);
        if (optReaction.isPresent() && optReaction.get().getPositive().equals(positive)) {
            throw new ReactionStateException(
                    String.format("User with id=%d already left reaction positive=%s to event with id=%d",
                            userId, positive, event.getId()));
        } else if (optReaction.isPresent()) {
            reaction = optReaction.get();
            reaction.setPositive(positive);
        } else {
            reaction = reactionMapper.mapToReaction(user, event, positive);
        }
        reactionRepository.save(reaction);
        reactionRepository.findReactionInfo(eventId).ifPresent(
                reactionView -> {
                    event.setLikes(reactionView.getLikes().orElse(0L));
                    event.setDislikes(reactionView.getDislikes().orElse(0L));
                }
        );
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public void delete(long eventId, long userId, boolean positive) {
        verifyUserId(userId);
        verifyEventId(eventId);
        Reaction reaction = reactionRepository.findByEventIdAndUserId(eventId, userId).orElseThrow(() ->
                new ReactionNotFoundException(String.format(
                        "User with id=%d dont react on event with id=%d yet", userId, eventId))
        );
        if (reaction.getPositive() != positive) {
            throw new ReactionStateException(String.format(
                    "Cant remove reaction=%s from user with id=%d on event with id=%d because reaction has state=%s",
                    positive, userId, eventId, reaction.getPositive()));
        } else {
            reactionRepository.delete(reaction);
        }
        log.info("Remove reaction={} from user with id={} on event with id={}", positive, userId, eventId);
    }

    @Override
    public Map<Long, Long> getPopularEvents(Long categoryId, int from, int size) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", categoryId));
        } else {
            return reactionRepository.findReactionsByCategory(categoryId, PageRequest.of(from, size))
                    .stream()
                    .collect(Collectors.toMap(LikesView::getId, LikesView::getCount));
        }
    }

    @Override
    public Map<Long, Long> getPopularEvents(CategoriesDto catDto, int from, int size) {
        return reactionRepository.findPopularEvents(catDto.getCategoryIds(), catDto.getCategoryNames(),
                        PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toMap(LikesView::getId, LikesView::getCount));
    }

    @Override
    public Map<Long, Long> getPopularByPartName(String text, int from, int size) {
        return reactionRepository.findPopularEvents(text, PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toMap(LikesView::getId, LikesView::getCount));
    }

    @Override
    public Map<Long, Long> getPopularInitiators(int from, int size) {
        return reactionRepository.findPopularInitiators(PageRequest.of(from, size))
                .stream()
                .collect(Collectors.toMap(LikesView::getId, LikesView::getCount));
    }

    @Override
    public Reactions getReactions(long eventId) {
        return reactionRepository.findReactionInfo(eventId)
                .map(view -> new Reactions(view.getEventId().orElse(eventId),
                        view.getLikes().orElse(0L),
                        view.getDislikes().orElse(0L)))
                .orElse(new Reactions(eventId, 0L, 0L));
    }

    private void verifyUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with id=%d was not found", userId));
        }
    }

    private void verifyEventId(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        }
    }

}
