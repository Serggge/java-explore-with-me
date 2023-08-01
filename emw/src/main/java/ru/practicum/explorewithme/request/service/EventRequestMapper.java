package ru.practicum.explorewithme.request.service;

import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.model.Request;
import java.util.List;

public interface EventRequestMapper {

    ParticipationRequestDto mapToDto(Request request);

    List<ParticipationRequestDto> mapToDto(Iterable<Request> requests);

}
