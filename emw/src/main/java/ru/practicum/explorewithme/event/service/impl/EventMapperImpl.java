package ru.practicum.explorewithme.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.service.CatergoryMapper;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.LocationDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.service.EventMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserMapper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventMapperImpl implements EventMapper {

    private final CatergoryMapper catergoryMapper;
    private final UserMapper userMapper;

    @Autowired
    public EventMapperImpl(CatergoryMapper catergoryMapper, UserMapper userMapper) {
        this.catergoryMapper = catergoryMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Event mapToUpdatedEvent(NewEventDto dto, Map<String, Object> dependencies) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setAnnotation(dto.getAnnotation());
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        event.setCreated(created);
        event.setEventDate(dto.getEventDate());
        Category category = (Category) dependencies.get("category");
        event.setCategory(category);
        User initiator = (User) dependencies.get("initiator");
        event.setInitiator(initiator);
        event.setLat(dto.getLocation().getLat());
        event.setLon(dto.getLocation().getLon());
        event.setState(EventState.PENDING);
        event.setPaid(dto.isPaid());
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }
        event.setParticipantLimit(dto.getParticipantLimit());
        return event;
    }

    @Override
    public Event mapToUpdatedEvent(UpdateEventRequest dto, Event event, Map<String, Object> dependencies) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            Category category = (Category) dependencies.get("category");
            event.setCategory(category);
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLat(dto.getLocation().getLat());
            event.setLon(dto.getLocation().getLon());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublished(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    event.setPublished(null);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    event.setPublished(null);
                    break;
            }
        }
        return event;
    }

    @Override
    public EventFullDto mapToFullDto(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(catergoryMapper.mapToDto(event.getCategory()));
        dto.setCreatedOn(event.getCreated());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setId(event.getId());
        dto.setInitiator(userMapper.mapToShortDto(event.getInitiator()));
        dto.setLocation(new LocationDto(event.getLat(), event.getLon()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublished());
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState().name());
        dto.setTitle(event.getTitle());
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setViews(event.getViews());
        return dto;
    }

    @Override
    public List<EventFullDto> mapToFullDto(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();
        for (Event event : events) {
            result.add(mapToFullDto(event));
        }
        return result;
    }

    @Override
    public EventShortDto mapToShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(catergoryMapper.mapToDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(userMapper.mapToShortDto(event.getInitiator()));
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    @Override
    public List<EventShortDto> mapToShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            result.add(mapToShortDto(event));
        }
        return result;
    }
}
