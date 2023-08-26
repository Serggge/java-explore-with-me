package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import java.util.List;
import java.util.Map;

public interface StatClient {

    ViewStats sendHit(EndpointHit hitDto);

    List<ViewStats> getStatisticByParams(Map<String, String> params);

}
