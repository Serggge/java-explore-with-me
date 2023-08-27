package ru.practicum.explorewithme.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.service.StatMapper;
import java.time.LocalDateTime;

@Component
public class StatMapperImpl implements StatMapper {

    @Override
    public Statistic mapToStatistic(EndpointHit hitDto, Application app) {
        Statistic statistic = new Statistic();
        statistic.setApp(app);
        statistic.setIp(hitDto.getIp());
        LocalDateTime timestamp = hitDto.getTimestamp();
        statistic.setTimestamp(timestamp);
        return statistic;
    }

}
