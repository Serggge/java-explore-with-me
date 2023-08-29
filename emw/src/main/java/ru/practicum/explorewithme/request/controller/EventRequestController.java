package ru.practicum.explorewithme.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.service.EventRequestService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
@Validated
public class EventRequestController {

    private final EventRequestService eventRequestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createEventRequest(@PathVariable @Min(1) Long userId,
                                                      @RequestParam @Min(1) Long eventId) {
        log.debug("Post for new EventRequest: userId={}, eventId={}", userId, eventId);
        return eventRequestService.create(userId, eventId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> returnUserRequests(@PathVariable @Min(1) Long userId) {
        log.debug("request for getting user's Requests by userId={}", userId);
        return eventRequestService.getByUserId(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(@PathVariable @Min(1) Long userId,
                                                      @PathVariable @Min(1) Long requestId) {
        log.debug("Request for canceling event request. userId={}, requestId={}", userId, requestId);
        return eventRequestService.delete(userId, requestId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> returnRequestsUserEvent(@PathVariable @Min(1) Long userId,
                                                                 @PathVariable @Min(1) Long eventId) {
        log.debug("Request for getting requests for event, created by user: userId={}, eventId={}", userId, eventId);
        return eventRequestService.getRequestsUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable @Min(1) Long userId,
                                                               @PathVariable @Min(1) Long eventId,
                                                               @RequestBody @Valid EventRequestStatusUpdateRequest dto) {
        log.debug("Request for change requests status. userId={}, eventId={}, requests={}", userId, eventId, dto);
        return eventRequestService.updateRequestsStatus(userId, eventId, dto);
    }

}
