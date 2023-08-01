package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import java.util.List;
import java.util.Map;

public interface EventMapper {

    Event mapToUpdatedEvent(NewEventDto dto, Map<String, Object> dependencies);

    Event mapToUpdatedEvent(UpdateEventRequest dto, Event event, Map<String, Object> dependencies);

    EventFullDto mapToFullDto(Event event);

    List<EventFullDto> mapToFullDto(Iterable<Event> events);

    EventShortDto mapToShortDto(Event event);

    List<EventShortDto> mapToShortDto(Iterable<Event> events);
}
