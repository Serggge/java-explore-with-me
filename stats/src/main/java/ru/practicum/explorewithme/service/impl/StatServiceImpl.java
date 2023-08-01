package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.Application;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.repository.AppRepository;
import ru.practicum.explorewithme.repository.StatRepository;
import ru.practicum.explorewithme.service.StatMapper;
import ru.practicum.explorewithme.service.StatService;
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
    public void addHit(HitDto hitDto) {
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
    }

    @Override
    public List<StatisticDto> getStatistic(String start, String end, String[] uris, Boolean unique) {
        log.debug("Запрошена статистика с {} по {} для url: {}, уникальность={}", start, end, uris, unique);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        if (unique) {
            return uris == null ? statMapper.mapToDto(statRepository.findUniqueStatistic(startTime, endTime))
                    : statMapper.mapToDto(statRepository.findUniqueStatistic(startTime, endTime, uris));
        } else {
            return uris == null ? statMapper.mapToDto(statRepository.findStatistic(startTime, endTime))
                    : statMapper.mapToDto(statRepository.findStatistic(startTime, endTime, uris));
        }
    }

}
