package ru.practicum.explorewithme.request.service;

import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import java.util.List;

public interface EventRequestService {

    ParticipationRequestDto create(long userId, long eventId);

    List<ParticipationRequestDto> getByUserId(long userId);

    ParticipationRequestDto delete(long userId, long requestId);

    List<ParticipationRequestDto> getRequestsUserEvent(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(long userId, long eventId,
                                                        EventRequestStatusUpdateRequest dto);

}
