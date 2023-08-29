package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import static ru.practicum.explorewithme.util.Constants.DATE_PATTERN;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping("/events/{id}")
    public EventFullDto returnEventById(@PathVariable @Min(1) Long id,
                                        HttpServletRequest servletRequest) {
        log.debug("Request for getting event by id={}", id);
        return eventService.getEventById(id, servletRequest);
    }


    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> returnAllEventsAddedByUser(@PathVariable @Min(1) Long userId,
                                                          @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                          @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Request for getting Events Added by User ID={}, from={}, size={}", userId, from, size);
        return eventService.getAllEventsAddedByUser(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @Min(1) Long userId,
                                    @RequestBody @Valid NewEventDto requestDto) {
        log.debug("Request for creation Event by User with ID={}, event={}", userId, requestDto);
        return eventService.add(userId, requestDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto returnEventAddedByUser(@PathVariable @Min(1) Long userId,
                                               @PathVariable @Min(1) Long eventId,
                                               HttpServletRequest servletRequest) {
        log.debug("Request for getting User's Event, userID={}, eventID={}", userId, eventId);
        return eventService.getEventAddedByUser(userId, eventId, servletRequest);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable @Min(1) Long userId,
                                          @PathVariable @Min(1) Long eventId,
                                          @RequestBody @Valid UpdateEventRequest dto) {
        log.debug("User: request for patch Event. UserID={}, changes={}", userId, dto);
        return eventService.updateEventUserRequest(userId, eventId, dto);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> returnAllEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<EventState> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_PATTERN)
                                                   LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_PATTERN)
                                                   LocalDateTime rangeEnd,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("Request for getting all Admin request, users={}, states={}, categories={}, rangeStart={}," +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsAdminRequest(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/events")
    public List<EventShortDto> returnAllEventsPublic(@RequestParam(required = false) @Length(min = 3) String text,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(pattern = DATE_PATTERN)
                                                     LocalDateTime rangeStart,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(pattern = DATE_PATTERN)
                                                     LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                     @RequestParam(required = false) Sorting sort,
                                                     @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                     @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                     HttpServletRequest servletRequest) {
        log.debug("Request for getting all Public request. text={}, categories={}, paid={}, rangeStart={}," +
                        "rangeEnd={}, available={}, sorting={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEventsPublicRequest(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, servletRequest);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable @Min(1) Long eventId,
                                           @RequestBody @Valid UpdateEventRequest dto) {
        log.debug("Admin: request for patch Event. changes={}", dto);
        return eventService.updateEventAdminRequest(eventId, dto);
    }

    @GetMapping("/admin/events/all")
    public List<EventFullDto> returnAll() {
        return eventService.getAll();
    }

    @GetMapping("/popular/category/{categoryId}")
    public List<EventShortDto> returnPopularEvents(@PathVariable Long categoryId,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request getting popular events by category with id={}, from={}, size={}", categoryId, from, size);
        return eventService.getPopularEvents(categoryId, from, size);
    }

    @GetMapping("/popular/category")
    public List<EventShortDto> returnPopularEvents(@RequestBody CategoriesDto categoriesDto,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request getting popular events by category list={}, from={}, size={}", categoriesDto, from, size);
        return eventService.getPopularEvents(categoriesDto, from, size);
    }

    @GetMapping("/popular")
    public List<EventShortDto> returnPopularByPartEventName(@RequestParam @Length(min = 2) String text,
                                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request get popular events by part of name={}, from={}, size={}", text, from, size);
        return eventService.getPopularByPartName(text, from, size);
    }
}
