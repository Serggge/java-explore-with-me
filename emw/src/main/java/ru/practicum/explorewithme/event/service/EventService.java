package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.Sorting;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventShortDto> getAllEventsAddedByUser(long userId, int from, int size);

    EventFullDto add(long userId, NewEventDto dto);

    EventFullDto getEventAddedByUser(long userId, long eventId, HttpServletRequest servletRequest);

    EventFullDto updateEventUserRequest(long userId, long eventId, UpdateEventRequest dto);

    EventFullDto updateEventAdminRequest(long eventId, UpdateEventRequest dto);

    List<EventFullDto> getEventsAdminRequest(List<Long> userIds, List<EventState> states, List<Long> categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsPublicRequest(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, Sorting sort, Integer from,
                                               Integer size, HttpServletRequest servletRequest);

    EventFullDto getEventById(long eventId, HttpServletRequest servletRequest);

    Event getEventEntityById(long eventId);

    List<Event> getEvents(Iterable<Long> ids);

    List<EventFullDto> getAll();
}
