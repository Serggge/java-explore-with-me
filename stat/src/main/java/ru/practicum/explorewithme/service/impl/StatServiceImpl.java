package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.model.Statistic;
import ru.practicum.explorewithme.repository.StatRepository;
import ru.practicum.explorewithme.service.StatMapper;
import ru.practicum.explorewithme.service.StatService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Override
    public void addHit(HitDto hitDto) {
        Statistic statistic = statMapper.mapToStatistic(hitDto);
        statRepository.save(statistic);
    }

    @Override
    public List<StatisticDto> getStatistic(String start, String end, String[] uris, Boolean unique) {
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
