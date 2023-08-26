package ru.practicum.explorewithme.event.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category_;
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
import ru.practicum.explorewithme.event.model.Event_;
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
import ru.practicum.explorewithme.user.model.User_;
import ru.practicum.explorewithme.user.service.UserService;
import static ru.practicum.explorewithme.util.Constants.DATE_FORMAT;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Setter
@Slf4j
public class EventServiceImpl implements EventService {

    @PersistenceContext
    private final EntityManager em;
    private final CategoryService categoryService;
    private final UserService userService;
    private final StatProxyService statService;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EntityManager em,
                            CategoryService categoryService,
                            UserService userService,
                            StatProxyService statService,
                            EventRepository eventRepository,
                            EventRequestRepository eventRequestRepository,
                            EventMapper eventMapper) {
        this.em = em;
        this.categoryService = categoryService;
        this.userService = userService;
        this.statService = statService;
        this.eventRepository = eventRepository;
        this.eventRequestRepository = eventRequestRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventFullDto getEventById(long eventId, HttpServletRequest servletRequest) {
        Event foundedEvent = eventRepository.findByIdAndPublishedNotNull(eventId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
        ViewStats statDto = statService.addHit(servletRequest);
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
        ViewStats statDto = statService.addHit(servletRequest);
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
                                                    String rangeStart, String rangeEnd, Integer from, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch(Event_.category, JoinType.INNER);
        root.fetch(Event_.initiator, JoinType.INNER);

        cq.select(root);

        List<Predicate> conditions = new ArrayList<>();
        if (userIds != null) {
            conditions.add(root.get(Event_.initiator).get(User_.id).in(userIds));
        }
        if (states != null) {
            conditions.add(root.get(Event_.state).in(states));
        }
        if (categories != null) {
            conditions.add(root.get(Event_.category).get(Category_.id).in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            conditions.add(cb.between(root.get(Event_.eventDate), LocalDateTime.parse(rangeStart, DATE_FORMAT),
                    LocalDateTime.parse(rangeEnd, DATE_FORMAT)));
        }

        cq.where(conditions.toArray(new Predicate[0]));

        TypedQuery<Event> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        List<Event> resultList = typedQuery.getResultList();
        fillStatistic(resultList);
        fillRequestInfo(resultList);
        return eventMapper.mapToFullDto(resultList);
    }

    @Override
    public List<EventShortDto> getEventsPublicRequest(String text, List<Long> categories, Boolean paid,
                                                      String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                      Sorting sort, Integer from, Integer size,
                                                      HttpServletRequest servletRequest) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch(Event_.category, JoinType.INNER);
        root.fetch(Event_.initiator, JoinType.INNER);

        cq.select(root);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(cb.isNotNull(root.get(Event_.published)));
        if (text != null) {
            String templateForSearch = "%" + text.toUpperCase() + "%";
            Predicate textInAnnotation = cb.like(cb.upper(root.get(Event_.annotation)), templateForSearch);
            Predicate textInDescription = cb.like(cb.upper(root.get(Event_.description)), templateForSearch);
            conditions.add(cb.or(textInAnnotation, textInDescription));
        }
        if (categories != null) {
            conditions.add(root.get(Event_.category).get(Category_.id).in(categories));
        }
        if (paid != null) {
            conditions.add(cb.equal(root.get(Event_.paid), paid));
        }

        if (rangeStart == null || rangeEnd == null) {
            conditions.add(cb.greaterThan(root.get(Event_.eventDate), LocalDateTime.now()));
        } else {
            conditions.add(cb.between(root.get(Event_.eventDate), LocalDateTime.parse(rangeStart, DATE_FORMAT),
                    LocalDateTime.parse(rangeEnd, DATE_FORMAT)));
        }

        if (sort != null && sort.equals(Sorting.EVENT_DATE)) {
            cq = cq.where(conditions.toArray(new Predicate[0])).orderBy(cb.asc(root.get(Event_.eventDate)));
        } else {
            cq = cq.where(conditions.toArray(new Predicate[0]));
        }

        TypedQuery<Event> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        List<Event> resultList = typedQuery.getResultList();
        fillStatistic(resultList);
        fillRequestInfo(resultList);

        if (onlyAvailable && sort == Sorting.VIEWS) {
            resultList = resultList.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .sorted(Comparator.comparingLong(Event::getViews).reversed())
                    .collect(Collectors.toList());
        } else if (!onlyAvailable && sort == Sorting.VIEWS) {
            resultList = resultList.stream()
                    .sorted(Comparator.comparingLong(Event::getViews).reversed())
                    .collect(Collectors.toList());
        } else if (sort == Sorting.VIEWS) {
            resultList = resultList.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        statService.addHit(servletRequest);
        return eventMapper.mapToShortDto(resultList);
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
        String[] uri = new String[]{"/events/" + event.getId()};
        List<ViewStats> statistic = statService.getStatistic(
                LocalDateTime.now().minusYears(1), LocalDateTime.now(), uri, false);
        if (statistic.size() == 1) {
            event.setViews(statistic.get(0).getHits());
        } else {
            event.setViews(0);
        }
    }

    private void fillStatistic(Collection<Event> events) {
        String[] uris = events
                .stream()
                .map(event -> "/events/" + event.getId())
                .toArray(String[]::new);
        Map<String, ViewStats> statMap = statService.getStatistic(
                        LocalDateTime.now().minusYears(1), LocalDateTime.now(), uris, true)
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
        Map<Long, Long> requestsMap = em.createQuery(
                        "select event.id as eventId, count(request.id) as count " +
                                "from Request request " +
                                "join request.event event " +
                                "where event.id in :eventsIds " +
                                "group by event.id", Tuple.class)
                .setParameter("eventsIds", eventsIds)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> ((Number) tuple.get("eventId")).longValue(),
                        tuple -> ((Number) tuple.get("count")).longValue()
                ));
        for (Event event : events) {
            event.setConfirmedRequests(requestsMap.getOrDefault(event.getId(), 0L));
        }
    }

}
