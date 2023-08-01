package ru.practicum.explorewithme.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.CountByApp;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.service.StatMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StatMapperImpl implements StatMapper {

    @Override
    public Statistic mapToStatistic(HitDto hitDto, Application app) {
        Statistic statistic = new Statistic();
        statistic.setApp(app);
        statistic.setIp(hitDto.getIp());
        LocalDateTime timestamp = hitDto.getTimestamp();
        statistic.setTimestamp(timestamp);
        return statistic;
    }

    @Override
    public StatisticDto mapToDto(CountByApp stats) {
        StatisticDto dto = new StatisticDto();
        dto.setUri(stats.getUri());
        dto.setApp(stats.getApp());
        dto.setHits(stats.getCount());
        return dto;
    }

    @Override
    public List<StatisticDto> mapToDto(Collection<CountByApp> stats) {
        List<StatisticDto> result = new ArrayList<>();
        for (CountByApp stat : stats) {
            result.add(mapToDto(stat));
        }
        return result;
    }
}
