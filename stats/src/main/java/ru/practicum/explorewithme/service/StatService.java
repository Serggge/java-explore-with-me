package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import java.util.List;

public interface StatService {

    ViewStats addHit(EndpointHit hitDto);

    List<ViewStats> getStatistic(String start, String end, String uris, Boolean unique);

}
