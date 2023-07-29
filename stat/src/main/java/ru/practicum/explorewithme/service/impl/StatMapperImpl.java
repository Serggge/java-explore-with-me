package ru.practicum.explorewithme.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.StatByUri;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.service.StatMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class StatMapperImpl implements StatMapper {

    @Override
    public Statistic mapToStatistic(HitDto hitDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(hitDto.getApp());
        statistic.setUri(hitDto.getUri());
        statistic.setIp(hitDto.getIp());
        statistic.setTimestamp(hitDto.getTimestamp());
        return statistic;
    }

    @Override
    public StatisticDto mapToDto(StatByUri statByUri) {
        StatisticDto dto = new StatisticDto();
        dto.setUri(statByUri.getUri());
        dto.setApp(statByUri.getApp());
        dto.setHits(statByUri.getCount());
        return dto;
    }

    @Override
    public List<StatisticDto> mapToDto(Collection<StatByUri> statByUris) {
        List<StatisticDto> result = new ArrayList<>();
        for (StatByUri stat : statByUris) {
            result.add(mapToDto(stat));
        }
        return result;
    }
}
