package ru.practicum.explorewithme.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.event.dto.Categorized;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.Sorting;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.model.StateAction;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.illegal.EventStateException;
import ru.practicum.explorewithme.exception.illegal.UserMatchingException;
import ru.practicum.explorewithme.exception.illegal.TimeLimitException;
import ru.practicum.explorewithme.exception.notFound.EventNotFoundException;
import ru.practicum.explorewithme.request.repository.EventRequestRepository;
import ru.practicum.explorewithme.statistic.StatProxyService;
import ru.practicum.explorewithme.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import static ru.practicum.explorewithme.util.Constants.APP_NAME;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Setter
@Slf4j
public class EventServiceImpl implements EventService {

    private final CategoryService categoryService;
    private final UserService userService;
    private final StatProxyService statService;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto getEventById(long eventId, HttpServletRequest servletRequest) {
        Event foundedEvent = eventRepository.findByIdAndPublishedNotNull(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
        ViewStats statDto = statService.addHit(APP_NAME, "/events/" + eventId, servletRequest.getRemoteAddr());
        foundedEvent.setViews(statDto.getHits());
        fillRequestInfo(foundedEvent);
        return eventMapper.mapToFullDto(foundedEvent);
    }

    @Override
    public List<EventShortDto> getAllEventsAddedByUser(long userId, int from, int size) {
        log.debug("Getting all events added by user with ID={}, from={}, size={}", userId, from, size);
        userService.getUserById(userId);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size))
                .get()
                .collect(Collectors.toList());
        fillStatistic(events);
        fillRequestInfo(events);
        return eventMapper.mapToShortDto(events);
    }

    @Override
    public EventFullDto add(long userId, NewEventDto dto) {
        validateEventStartDate(dto.getEventDate(), LocalDateTime.now(), 2);
        Map<String, Object> dependenciesMap = collectDependencies(dto, userId);
        Event newEvent = eventMapper.mapToUpdatedEvent(dto, dependenciesMap);
        newEvent = eventRepository.save(newEvent);
        log.info("New Event created: {}", newEvent);
        return eventMapper.mapToFullDto(newEvent);
    }

    @Override
    public EventFullDto getEventAddedByUser(long userId, long eventId, HttpServletRequest servletRequest) {
        log.debug("Get Event by ID={}, userID={}", eventId, userId);
        Event userEvent = getUserEvent(userId, eventId);
        ViewStats statDto = statService.addHit(APP_NAME, "/events/" + eventId, servletRequest.getRemoteAddr());
        userEvent.setViews(statDto.getHits());
        fillRequestInfo(userEvent);
        return eventMapper.mapToFullDto(userEvent);
    }

    @Override
    public EventFullDto updateEventUserRequest(long userId, long eventId, UpdateEventRequest dto) {
        Event foundedEvent = getUserEvent(userId, eventId);
        if (foundedEvent.getState() == EventState.PENDING || foundedEvent.getState() == EventState.CANCELED) {
            validateEventStartDate(dto.getEventDate(), LocalDateTime.now(), 2);
            Map<String, Object> dependenciesMap = collectDependencies(dto, null);
            Event updatedEvent = eventMapper.mapToUpdatedEvent(dto, foundedEvent, dependenciesMap);
            updatedEvent = eventRepository.save(updatedEvent);
            log.info("Event updated: {}", updatedEvent);
            fillStatistic(updatedEvent);
            fillRequestInfo(updatedEvent);
            return eventMapper.mapToFullDto(updatedEvent);
        } else {
            throw new EventStateException("Only pending or canceled events can be changed");
        }

    }

    @Override
    public EventFullDto updateEventAdminRequest(long eventId, UpdateEventRequest dto) {
        Event foundedEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with ID=%d not found", eventId)));
        validateEventStartDate(dto.getEventDate(), foundedEvent.getPublished(), 1);
        Map<String, Object> dependenciesMap = collectDependencies(dto, null);
        if (dto.getStateAction() == StateAction.PUBLISH_EVENT && foundedEvent.getState() != EventState.PENDING) {
            throw new EventStateException(
                    String.format("Cannot publish the event because it's not in the right state: %s",
                            foundedEvent.getState().name()));
        } else if (dto.getStateAction() == StateAction.REJECT_EVENT && foundedEvent.getState() == EventState.PUBLISHED) {
            throw new EventStateException(
                    String.format("Cannot reject the event because it's not in the right state: %s",
                            foundedEvent.getState().name()));
        } else {
            Event updatedEvent = eventMapper.mapToUpdatedEvent(dto, foundedEvent, dependenciesMap);
            updatedEvent = eventRepository.save(updatedEvent);
            log.info("Event updated: {}", updatedEvent);
            fillStatistic(updatedEvent);
            fillRequestInfo(updatedEvent);
            return eventMapper.mapToFullDto(updatedEvent);
        }
    }

    @Override
    public List<EventFullDto> getEventsAdminRequest(List<Long> userIds, List<EventState> states, List<Long> categories,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Integer from, Integer size) {
        List<Event> events = eventRepository.findEventsAdminRequest(userIds, states, categories, rangeStart, rangeEnd, from, size);
        fillStatistic(events);
        fillRequestInfo(events);
        return eventMapper.mapToFullDto(events);
    }

    @Override
    public List<EventShortDto> getEventsPublicRequest(String text, List<Long> categories, Boolean paid,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Boolean onlyAvailable, Sorting sort, Integer from, Integer size,
                                                      HttpServletRequest servletRequest) {
        List<Event> events = eventRepository.findEventsPublicRequest(text, categories, paid, rangeStart, rangeEnd, sort, from, size);
        if (onlyAvailable && sort == Sorting.VIEWS) {
            events = events.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit()
                            || event.getRequestModeration() == Boolean.FALSE)
                    .sorted(Comparator.comparingLong(Event::getViews).reversed())
                    .collect(Collectors.toList());
        } else if (!onlyAvailable && sort == Sorting.VIEWS) {
            events = events.stream()
                    .sorted(Comparator.comparingLong(Event::getViews).reversed())
                    .collect(Collectors.toList());
        } else if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit()
                            || event.getRequestModeration() == Boolean.FALSE)
                    .collect(Collectors.toList());
        }
        statService.addHit(APP_NAME, "/events", servletRequest.getRemoteAddr());
        fillStatistic(events);
        fillRequestInfo(events);
        return eventMapper.mapToShortDto(events);
    }

    @Override
    public Event getEventEntityById(long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with ID=%d not found", eventId)));
        fillStatistic(event);
        fillRequestInfo(event);
        return event;
    }

    @Override
    public List<Event> getEvents(Iterable<Long> ids) {
        List<Event> events = eventRepository.findAllByIds(ids);
        fillStatistic(events);
        fillRequestInfo(events);
        return events;
    }

    private void validateEventStartDate(LocalDateTime dtoEventDate, LocalDateTime otherDate, int hours) {
        if (dtoEventDate != null && otherDate != null && dtoEventDate.isBefore(otherDate.plusHours(hours))) {
            throw new TimeLimitException("Less than two hours before start of event");
        }
    }

    private Map<String, Object> collectDependencies(Categorized categorized, Long userId) {
        Map<String, Object> dependenciesMap = new HashMap<>();
        if (categorized.getCategory() != null) {
            dependenciesMap.put("category", categoryService.getCategoryById(categorized.getCategory()));
        }
        if (userId != null) {
            dependenciesMap.put("initiator", userService.getUserById(userId));
        }
        return dependenciesMap;
    }

    private Event getUserEvent(long userId, long eventId) {
        Event foundedEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (foundedEvent.getInitiator().getId() != userId) {
            throw new UserMatchingException(
                    String.format("User with id=%d is not initiator of Event with ID=%d", userId, eventId));
        }
        return foundedEvent;
    }

    @Override
    public List<EventFullDto> getAll() {
        return eventMapper.mapToFullDto(eventRepository.findAll())
                .stream()
                .sorted(Comparator.comparingLong(EventFullDto::getId))
                .collect(Collectors.toList());
    }

    private void fillStatistic(Event event) {
        if (event.getPublished() != null) {
            Set<String> uri = Collections.singleton("/events/" + event.getId());
            List<ViewStats> statistic = statService.getStatistic(event.getPublished(), LocalDateTime.now(), uri, false);
            if (statistic.size() == 1) {
                event.setViews(statistic.get(0).getHits());
            } else {
                event.setViews(0);
            }
        } else {
            event.setViews(0);
        }
    }

    private void fillStatistic(Collection<Event> events) {
        events.stream()
                .map(Event::getPublished)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .ifPresent(firstEventDate -> {
                    Map<String, ViewStats> statMap = statService.getStatistic(
                                    firstEventDate, LocalDateTime.now(), makeUri(events), true)
                            .stream()
                            .collect(Collectors.toMap(ViewStats::getUri, stat -> stat));
                    for (Event event : events) {
                        String appUri = "/events/" + event.getId();
                        if (statMap.containsKey(appUri)) {
                            event.setViews(statMap.get(appUri).getHits());
                        } else {
                            event.setViews(0);
                        }
                    }
                });
    }

    private Set<String> makeUri(Collection<Event> events) {
        return events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toSet());
    }

    private void fillRequestInfo(Event event) {
        Long confirmedCount = eventRequestRepository.getConfirmedCount(event.getId());
        event.setConfirmedRequests(confirmedCount);
    }

    private void fillRequestInfo(Collection<Event> events) {
        Set<Long> eventsIds = events
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Map<Long, Long> requestsMap = eventRepository.getRequestsCount(eventsIds);
        for (Event event : events) {
            event.setConfirmedRequests(requestsMap.getOrDefault(event.getId(), 0L));
        }
    }

}
