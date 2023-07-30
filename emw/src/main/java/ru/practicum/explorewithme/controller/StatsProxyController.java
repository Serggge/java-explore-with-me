package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.service.StatProxyService;
import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
public class StatsProxyController {

    private final StatProxyService statService;

    @GetMapping
    public List<StatisticDto> view(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) String[] uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        log.debug("StatsProxyController got request for statistics: " +
                "start: {} end: {} uris: {} unique: {}", start, end, uris, unique);
        return statService.getStatistic(start, end, uris, unique);
    }
}
