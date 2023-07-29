package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class StatsController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(HitDto hitDto) {
        statService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatisticDto> view(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) String[] uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        return statService.getStatistic(start, end, uris, unique);
    }

}
