package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.service.StatService;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@RequestMapping("/")
@Slf4j
public class StatsController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody HitDto hitDto) {
        log.debug("New hit: {}", hitDto);
        statService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatisticDto> view(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) String[] uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        log.debug("Getting statistic: start: {} end: {} uris: {} unique: {}", start, end, uris, unique);
        return statService.getStatistic(start, end, uris, unique);
    }

}
