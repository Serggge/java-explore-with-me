package ru.practicum.explorewithme.controller;

import lombok.NoArgsConstructor;
import lombok.Setter;
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
import ru.practicum.explorewithme.dto.ListStatisticDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.service.StatService;
import java.util.List;

@RestController
@NoArgsConstructor
@Setter
@RequestMapping("/")
@Slf4j
public class StatsController {

    private StatService statService;

    @Autowired
    public StatsController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatisticDto hit(@RequestBody HitDto hitDto) {
        log.debug("New hit: {}", hitDto);
        return statService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public ListStatisticDto view(@RequestParam(required = false) String start,
                                 @RequestParam(required = false) String end,
                                 @RequestParam(required = false) String uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.debug("Getting statistic: start: {} end: {} uris: {} unique: {}", start, end, uris, unique);
        List<StatisticDto> statisticList = statService.getStatistic(start, end, uris, unique);
        return new ListStatisticDto(statisticList);
    }

}
