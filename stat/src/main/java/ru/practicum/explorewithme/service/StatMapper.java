package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.StatByUri;
import ru.practicum.explorewithme.model.Statistic;
import java.util.Collection;
import java.util.List;

public interface StatMapper {

    Statistic mapToStatistic(HitDto hitDto);

    StatisticDto mapToDto(StatByUri statByUri);

    List<StatisticDto> mapToDto(Collection<StatByUri> statByUris);

}
