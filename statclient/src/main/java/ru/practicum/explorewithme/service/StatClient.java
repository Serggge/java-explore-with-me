package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import java.util.List;
import java.util.Map;

public interface StatClient {

    HitDto sendHit(HitDto hitDto);

    List<StatisticDto> getStatisticByParams(Map<String, String> params);

}
