package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import java.util.List;

public interface StatService {

    StatisticDto addHit(HitDto hitDto);

    List<StatisticDto> getStatistic(String start, String end, String uris, Boolean unique);

}
