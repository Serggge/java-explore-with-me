package ru.practicum.explorewithme.reaction.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.reaction.service.ReactionService;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
@Setter
@Getter
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/events/{eventId}/users/{userId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto reactToEvent(@PathVariable Long eventId,
                                     @PathVariable Long userId,
                                     @RequestParam Boolean positive) {
        log.debug("Request add reaction to event with id={}, user with id={}, positive={}", eventId, userId, positive);
        return reactionService.add(eventId, userId, positive);
    }

    @DeleteMapping("/events/{eventId}/users/{userId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReact(@PathVariable Long eventId,
                            @PathVariable Long userId,
                            @RequestParam Boolean positive) {
        log.debug("Request remove reaction to event with id={}, user with id={}, positive={}", eventId, userId, positive);
        reactionService.delete(eventId, userId, positive);
    }

    @GetMapping("/popular")
    public List<EventShortDto> returnPopularByPartEventName(@RequestParam @Length(min = 2) String text,
                                                            @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                            @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request get popular events by part of name={}, from={}, size={}", text, from, size);
        return reactionService.getPopularByPartName(text, from, size);
    }

    @GetMapping("/popular/category")
    public List<EventShortDto> returnPopularEvents(@RequestBody CategoriesDto categoriesDto,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request getting popular events by category list={}, from={}, size={}", categoriesDto, from, size);
        return reactionService.getPopularEvents(categoriesDto, from, size);
    }

    @GetMapping("/popular/category/{categoryId}")
    public List<EventShortDto> returnPopularEvents(@PathVariable Long categoryId,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request getting popular events by category with id={}, from={}, size={}", categoryId, from, size);
        return reactionService.getPopularEvents(categoryId, from, size);
    }

    @GetMapping("/popular/initiator")
    public List<UserShortDto> returnPopularInitiators(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.debug("Request getting popular initiators, from={}, size={}", from, size);
        return reactionService.getPopularInitiators(from, size);
    }

}
