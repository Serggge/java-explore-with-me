package ru.practicum.explorewithme.reaction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.reaction.service.ReactionService;
import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
@Validated
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/events/{eventId}/users/{userId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto reactToEvent(@PathVariable @Min(1) Long eventId,
                                     @PathVariable @Min(1) Long userId,
                                     @RequestParam Boolean positive) {
        log.debug("Request add reaction to event with id={}, user with id={}, positive={}", eventId, userId, positive);
        return reactionService.add(eventId, userId, positive);
    }

    @DeleteMapping("/events/{eventId}/users/{userId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReact(@PathVariable @Min(1) Long eventId,
                            @PathVariable @Min(1) Long userId,
                            @RequestParam Boolean positive) {
        log.debug("Request remove reaction to event with id={}, user with id={}, positive={}", eventId, userId, positive);
        reactionService.delete(eventId, userId, positive);
    }

}
