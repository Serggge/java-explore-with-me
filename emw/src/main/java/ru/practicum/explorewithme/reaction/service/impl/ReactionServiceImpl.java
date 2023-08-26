package ru.practicum.explorewithme.reaction.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.illegal.ReactionStateException;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.reaction.model.Popularity;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.repository.ReactionRepository;
import ru.practicum.explorewithme.reaction.service.ReactionMapper;
import ru.practicum.explorewithme.reaction.service.ReactionService;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserMapper;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final UserService userService;
    private final EventService eventService;
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Override
    public EventFullDto add(long eventId, long userId, boolean positive) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventEntityById(eventId);
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
        fillPopularity(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public void delete(long eventId, long userId, boolean positive) {

    }

    @Override
    public List<EventShortDto> getPopularEvents(Long categoryId, int from, int size) {
        return null;
    }

    @Override
    public List<EventShortDto> getPopularEvents(CategoriesDto catDto, int from, int size) {
        return null;
    }

    @Override
    public List<EventShortDto> getPopularByPartName(String name, String type, int from, int size) {
        return null;
    }

    @Override
    public List<UserShortDto> getPopularInitiators(int from, int size) {
        return null;
    }

    @Override
    public Event fillPopularity(Event event) {
        Optional<Popularity> optPopularity = reactionRepository.getPopularity(event.getId());
        if (optPopularity.isPresent()) {
            Popularity popularity = optPopularity.get();
            if (popularity.getLikes() != null && popularity.getLikes() > 0) {
                event.setLikes(popularity.getLikes());
            } else {
                event.setLikes(0);
            }
            if (popularity.getDislikes() != null && popularity.getDislikes() > 0) {
                event.setDislikes(popularity.getDislikes());
            } else {
                event.setDislikes(0);
            }
        } else {
            event.setLikes(0);
            event.setDislikes(0);
        }
        return event;
    }

    @Override
    public List<Event> fillPopularity(Iterable<Event> events) {
        return null;
    }
}
