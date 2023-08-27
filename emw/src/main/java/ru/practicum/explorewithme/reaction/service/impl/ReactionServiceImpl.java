package ru.practicum.explorewithme.reaction.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.service.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.illegal.EventStateException;
import ru.practicum.explorewithme.exception.illegal.ReactionStateException;
import ru.practicum.explorewithme.exception.notFound.CategoryNotFoundException;
import ru.practicum.explorewithme.exception.notFound.ReactionNotFoundException;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.repository.Dislikes;
import ru.practicum.explorewithme.reaction.repository.Likes;
import ru.practicum.explorewithme.reaction.repository.ReactionRepository;
import ru.practicum.explorewithme.reaction.service.ReactionMapper;
import ru.practicum.explorewithme.reaction.service.ReactionService;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserMapper;
import ru.practicum.explorewithme.user.service.UserService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Setter
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final UserService userService;
    private final EventService eventService;
    private final CategoryRepository categoryRepository;
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Override
    public EventFullDto add(long eventId, long userId, boolean positive) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventEntityById(eventId);
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
        fillReactions(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public void delete(long eventId, long userId, boolean positive) {
        userService.getUserById(userId);
        eventService.getEventEntityById(eventId);
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
    public List<EventShortDto> getPopularEvents(Long categoryId, int from, int size) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", categoryId));
        } else {
            List<Event> events = reactionRepository.findPopularEvents(categoryId, PageRequest.of(from, size));
            return eventMapper.mapToShortDto(events);
        }
    }

    @Override
    public List<EventShortDto> getPopularEvents(CategoriesDto catDto, int from, int size) {
        Set<Event> commonSet = new HashSet<>();
        Set<Long> categoryIds = catDto.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            commonSet.addAll(reactionRepository.findPopularEvents(categoryIds, PageRequest.of(from, size)));
        }
        Set<String> categoryNames = catDto.getCategoryNames();
        if (categoryNames != null && !categoryNames.isEmpty()) {
            commonSet.addAll(reactionRepository.findPopularEvents(categoryNames, PageRequest.of(from, size)));
        }
        fillReactions(commonSet);
        List<Event> result = commonSet.stream()
                .sorted((e1, e2) -> Long.compare(e2.getLikes(), e1.getLikes()))
                .limit((size - from))
                .collect(Collectors.toList());
        return eventMapper.mapToShortDto(result);
    }

    @Override
    public List<EventShortDto> getPopularByPartName(String text, int from, int size) {
        List<Event> events = reactionRepository.findPopularEvents(text, PageRequest.of(from, size));
        return eventMapper.mapToShortDto(events);
    }

    @Override
    public List<UserShortDto> getPopularInitiators(int from, int size) {
        List<User> popularInitiators = reactionRepository.findPopularInitiators(PageRequest.of(from, size));
        return userMapper.mapToShortDto(popularInitiators);
    }

    private void fillReactions(Event event) {
        Likes likes = getLikes(event.getId());
        Dislikes dislikes = getDislikes(event.getId());
        event.setLikes(likes.getLikes());
        event.setDislikes(dislikes.getDislikes());
    }

    private void fillReactions(Collection<Event> events) {
        Set<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Map<Long, Long> likesCount = getLikesCount(eventIds);
        Map<Long, Long> dislikesCount = getDislikesCount(eventIds);
        events.forEach(event -> {
            event.setLikes(likesCount.getOrDefault(event.getId(), 0L));
            event.setDislikes(dislikesCount.getOrDefault(event.getId(), 0L));
        });
    }

    private Likes getLikes(long eventId) {
        return reactionRepository.findEventLikes(eventId).orElse(new Likes() {
            @Override
            public Long getEventId() {
                return eventId;
            }

            @Override
            public Long getLikes() {
                return 0L;
            }
        });
    }

    private Map<Long, Long> getLikesCount(Iterable<Long> eventIds) {
        return reactionRepository.findEventLikes(eventIds)
                .stream()
                .collect(Collectors.toMap(Likes::getEventId, Likes::getLikes));
    }

    private Dislikes getDislikes(long eventId) {
        return reactionRepository.findEventDislikes(eventId).orElse(new Dislikes() {
            @Override
            public Long getEventId() {
                return eventId;
            }

            @Override
            public Long getDislikes() {
                return 0L;
            }
        });
    }

    private Map<Long, Long> getDislikesCount(Iterable<Long> eventIds) {
        return reactionRepository.findEventDislikes(eventIds)
                .stream()
                .collect(Collectors.toMap(Dislikes::getEventId, Dislikes::getDislikes));
    }
}
