package ru.practicum.explorewithme.event.repository.dao;

import ru.practicum.explorewithme.event.dto.Sorting;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventDao {

    List<Event> findEventsAdminRequest(List<Long> userIds, List<EventState> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<Event> findEventsPublicRequest(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Sorting sort, Integer from, Integer size);

    Map<Long, Long> getRequestsCount(Iterable<Long> eventsIds);
}
