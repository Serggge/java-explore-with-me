package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.Statistic;

public interface StatMapper {

    Statistic mapToStatistic(EndpointHit hitDto, Application app);

}
