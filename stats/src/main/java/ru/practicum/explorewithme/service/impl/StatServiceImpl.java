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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String[] uriParts = hitDto.getUri().split("/");
        String appUri = "/" + uriParts[uriParts.length - 2] + "/" + uriParts[uriParts.length - 1];
        Optional<Application> optionalApp = appRepository.findByUriAndName(appUri, hitDto.getApp());
        Application app;
        if (optionalApp.isEmpty()) {
            app = new Application();
            app.setUri(appUri);
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
    public List<ViewStats> getStatistic(String start, String end, String uris, Boolean unique) {
        start = decode(start);
        end = decode(end);
        uris = decode(uris);
        String[] urisArray;
        if (uris.matches("^[.+,.+]$")) {
            urisArray = uris.substring(1, uris.length() - 1).split(",");
        } else if (uris.matches("^[.+]$")) {
            urisArray = new String[]{uris.substring(1, uris.length() - 1)};
        } else if (uris.contains(",")) {
            urisArray = uris.split(",");
        } else {
            urisArray = new String[]{uris};
        }
        log.debug("Запрошена статистика с {} по {} для url: {}, уникальность={}", start, end, uris, unique);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        if (endTime.isBefore(startTime)) {
            throw new DateTimeException("Start time can't be after end time");
        }
        List<ViewStats> stats;
        if (unique) {
            stats = statRepository.findUniqueStatistic(startTime, endTime, urisArray);
        } else {
            stats = statRepository.findStatistic(startTime, endTime, urisArray);
        }
        return stats;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}
