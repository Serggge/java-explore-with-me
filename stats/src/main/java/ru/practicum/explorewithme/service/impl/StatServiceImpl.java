package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.repository.AppRepository;
import ru.practicum.explorewithme.repository.StatRepository;
import ru.practicum.explorewithme.service.StatMapper;
import ru.practicum.explorewithme.service.StatService;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final AppRepository appRepository;
    private final StatMapper statMapper;

    @Override
    public ViewStats addHit(EndpointHit hitDto) {
        Optional<Application> optionalApp = appRepository.findByUriAndName(hitDto.getUri(), hitDto.getApp());
        Application app;
        if (optionalApp.isEmpty()) {
            app = new Application();
            app.setUri(hitDto.getUri());
            app.setName(hitDto.getApp());
            app = appRepository.save(app);
        } else {
            app = optionalApp.get();
        }
        Statistic statistic = statMapper.mapToStatistic(hitDto, app);
        Statistic saved = statRepository.save(statistic);
        log.info("Новое событие: {}", saved);
        return statRepository.findUniqueStatistic(app.getUri(), app.getName())
                .orElse(new ViewStats(app.getUri(), app.getName(), 0L));
    }

    @Override
    public List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.debug("Запрошена статистика с {} по {} для url: {}, уникальность={}", start, end, uris, unique);
        if (end.isBefore(start)) {
            throw new DateTimeException("Start time can't be after end time");
        }
        List<ViewStats> stats;
        if (unique) {
            stats = statRepository.findUniqueStatistic(start, end, uris);
        } else {
            stats = statRepository.findStatistic(start, end, uris);
        }
        return stats;
    }

}
