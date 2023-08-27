package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.service.StatService;
import java.time.LocalDateTime;
import java.util.List;
import static ru.practicum.explorewithme.utils.Constants.DATE_PATTERN;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@RequestMapping("/")
@Slf4j
public class StatsController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewStats hit(@RequestBody EndpointHit hitDto) {
        log.debug("New hit: {}", hitDto);
        return statService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> view(@RequestParam
                                @DateTimeFormat(pattern = DATE_PATTERN)
                                LocalDateTime start,
                                @RequestParam
                                @DateTimeFormat(pattern = DATE_PATTERN)
                                LocalDateTime end,
                                @RequestParam(required = false) List<String> uris,
                                @RequestParam(defaultValue = "false") Boolean unique) {
        log.debug("Getting statistic: start: {} end: {} uris: {} unique: {}", start, end, uris, unique);
        return statService.getStatistic(start, end, uris, unique);
    }

}
