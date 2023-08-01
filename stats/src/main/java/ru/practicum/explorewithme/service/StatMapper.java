package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.CountByApp;
import ru.practicum.explorewithme.model.Statistic;
import java.util.Collection;
import java.util.List;

public interface StatMapper {

    Statistic mapToStatistic(HitDto hitDto, Application app);

    StatisticDto mapToDto(CountByApp statByUri);

    List<StatisticDto> mapToDto(Collection<CountByApp> statByUris);

}
