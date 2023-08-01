package ru.practicum.explorewithme.request.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.service.EventRequestMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventRequestMapperImpl implements EventRequestMapper {

    @Override
    public ParticipationRequestDto mapToDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequester().getId());
        dto.setStatus(request.getStatus().toString());
        return dto;
    }

    @Override
    public List<ParticipationRequestDto> mapToDto(Iterable<Request> requests) {
        List<ParticipationRequestDto> result = new ArrayList<>();
        for (Request request : requests) {
            result.add(mapToDto(request));
        }
        return result;
    }
}
