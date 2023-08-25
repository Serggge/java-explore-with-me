package ru.practicum.explorewithme.request.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.illegal.EventStateException;
import ru.practicum.explorewithme.exception.illegal.RequestLimitException;
import ru.practicum.explorewithme.exception.illegal.RequestStateException;
import ru.practicum.explorewithme.exception.illegal.UserMatchingException;
import ru.practicum.explorewithme.exception.illegal.EventRequestExistException;
import ru.practicum.explorewithme.exception.notFound.EventNotFoundException;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.Status;
import ru.practicum.explorewithme.request.repository.EventRequestRepository;
import ru.practicum.explorewithme.request.service.EventRequestMapper;
import ru.practicum.explorewithme.request.service.EventRequestService;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventRequestServiceImpl implements EventRequestService {

    private final UserService userService;
    private final EventService eventService;
    private final EventRequestRepository eventRequestRepository;
    private final EventRequestMapper eventRequestMapper;

    @Autowired
    public EventRequestServiceImpl(UserService userService,
                                   EventService eventService,
                                   EventRequestRepository eventRequestRepository,
                                   EventRequestMapper eventRequestMapper) {
        this.userService = userService;
        this.eventService = eventService;
        this.eventRequestRepository = eventRequestRepository;
        this.eventRequestMapper = eventRequestMapper;
    }

    @Override
    public ParticipationRequestDto create(long requesterId, long eventId) {
        Event event = eventService.getEventEntityById(eventId);
        User requester = userService.getUserById(requesterId);
        if (eventRequestRepository.findByEventIdAndRequesterId(eventId, requesterId).isPresent()) {
            throw new EventRequestExistException(
                    String.format("User with ID=%d already sent request for event with ID=%d", requesterId, eventId));
        }
        if (event.getInitiator().getId() == requesterId) {
            throw new UserMatchingException(
                    String.format("Requester with ID=%d is initiator of event with ID=%d", requesterId, eventId));
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventStateException(String.format("Event with ID=%d is not published", eventId));
        }
        long confirmedRequests = eventRequestRepository.getConfirmedCount(eventId);
        if (event.getParticipantLimit() != 0 && confirmedRequests >= event.getParticipantLimit()) {
            throw new RequestLimitException(String.format("Request limit for event with ID=%d exceeded", eventId));
        }
        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(requester);
        newRequest.setCreated(LocalDateTime.now());
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            newRequest.setStatus(Status.CONFIRMED);
        } else {
            newRequest.setStatus(Status.PENDING);
        }
        newRequest = eventRequestRepository.save(newRequest);
        log.info("Created new Event Request: {}", newRequest);
        return eventRequestMapper.mapToDto(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> getByUserId(long userId) {
        log.debug("Getting all requests for user with ID={}", userId);
        User user = userService.getUserById(userId);
        List<Request> userRequests = eventRequestRepository.findAllByRequesterId(user.getId());
        return eventRequestMapper.mapToDto(userRequests);
    }

    @Override
    public ParticipationRequestDto delete(long userId, long requestId) {
        User user = userService.getUserById(userId);
        Request request = eventRequestRepository.findById(requestId).orElseThrow(() ->
                new EventNotFoundException(String.format("Request with ID=%d not found", requestId)));
        if (request.getRequester().getId() != userId) {
            throw new UserMatchingException("User ID not valid: " + user.getId());
        }
        request.setStatus(Status.CANCELED);
        eventRequestRepository.save(request);
        log.info("Event Request canceled: {}", request);
        return eventRequestMapper.mapToDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsUserEvent(long userId, long eventId) {
        log.debug("Getting all requests for initiator with ID={}, eventId={}", userId, eventId);
        User user = userService.getUserById(userId);
        Event event = eventService.getEventEntityById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new UserMatchingException("User ID not valid: " + user.getId());
        }
        List<Request> requests = eventRequestRepository.findAllByEventInitiatorId(userId);
        return eventRequestMapper.mapToDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestsStatus(long userId, long eventId,
                                                               EventRequestStatusUpdateRequest dto) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventEntityById(eventId);
        List<Request> requests = eventRequestRepository.findByIds(dto.getRequestIds());
        requests.stream()
                .filter(request -> request.getStatus() == Status.CONFIRMED)
                .findFirst()
                .ifPresent(request -> {
                    throw new RequestStateException("Cant change confirmed request");
                });
        switch (Status.valueOf(dto.getStatus().toUpperCase())) {
            case REJECTED:
                for (Request request : requests) {
                    request.setStatus(Status.REJECTED);
                }
                break;
            case CONFIRMED:
                long requestsCount = eventRequestRepository.getConfirmedCount(eventId);
                long requestsAvailable = event.getParticipantLimit() - requestsCount;
                if (requestsAvailable > 0) {
                    int count = requests.size();
                    while (count > 0 && requestsAvailable > 0) {
                        Request request = requests.get(requests.size() - count);
                        request.setStatus(Status.CONFIRMED);
                        count--;
                        requestsAvailable--;
                    }
                    while (count > 0) {
                        Request request = requests.get(requests.size() - count);
                        request.setStatus(Status.REJECTED);
                        count--;
                    }
                } else {
                    throw new RequestLimitException("Request limit exceeded");
                }
                break;
        }
        eventRequestRepository.saveAll(requests);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmedRequests = eventRequestMapper.mapToDto(requests.stream()
                .filter(request -> Status.CONFIRMED.equals(request.getStatus()))
                .collect(Collectors.toList()));
        result.setConfirmedRequests(confirmedRequests);
        List<ParticipationRequestDto> rejectedRequests = eventRequestMapper.mapToDto(requests.stream()
                .filter(request -> Status.REJECTED.equals(request.getStatus()))
                .collect(Collectors.toList()));
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

}
