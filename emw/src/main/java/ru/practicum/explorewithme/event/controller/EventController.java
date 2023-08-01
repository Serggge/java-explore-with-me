package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
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
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.Sorting;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.service.EventService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping("/events/{id}")
    public EventFullDto returnEventById(@PathVariable Long id,
                                        HttpServletRequest servletRequest) {
        log.debug("Request for getting event by id={}", id);
        return eventService.getEventById(id, servletRequest);
    }


    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> returnAllEventsAddedByUser(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                          @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Request for getting Events Added by User ID={}, from={}, size={}", userId, from, size);
        return eventService.getAllEventsAddedByUser(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto requestDto) {
        log.debug("Request for creation Event by User with ID={}, event={}", userId, requestDto);
        return eventService.add(userId, requestDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto returnEventAddedByUser(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               HttpServletRequest servletRequest) {
        log.debug("Request for getting User's Event, userID={}, eventID={}", userId, eventId);
        return eventService.getEventAddedByUser(userId, eventId, servletRequest);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Valid UpdateEventRequest dto) {
        log.debug("User: request for patch Event. UserID={}, changes={}", userId, dto);
        return eventService.updateEventUserRequest(userId, eventId, dto);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> returnAllEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<EventState> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Request for getting all Admin request, users={}, states={}, categories={}, rangeStart={}," +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsAdminRequest(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/events")
    public List<EventShortDto> returnAllEventsPublic(@RequestParam(required = false) @Length(min = 3) String text,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                     @RequestParam(required = false) Sorting sort,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size,
                                                     HttpServletRequest servletRequest) {
        log.debug("Request for getting all Public request. text={}, categories={}, paid={}, rangeStart={}," +
                        "rangeEnd={}, available={}, sorting={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEventsPublicRequest(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, servletRequest);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventRequest dto) {
        log.debug("Admin: request for patch Event. changes={}", dto);
       return eventService.updateEventAdminRequest(eventId, dto);
    }

    @GetMapping("/admin/events/all")
    public List<EventFullDto> returnAll() {
        return eventService.getAll();
    }
}
