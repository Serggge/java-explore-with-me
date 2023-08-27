package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    ViewStats addHit(EndpointHit hitDto);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
