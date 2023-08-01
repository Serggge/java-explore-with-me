package ru.practicum.explorewithme.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import ru.practicum.explorewithme.exception.ServerResponseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatProxyServiceImpl implements StatProxyService {

    private final StatClient statClient;

    public StatProxyServiceImpl(@Value("${explore.stats-server.url}") String serviceUrl) {
        this.statClient = StatsClientFactory.getClient(serviceUrl);
    }

    @Override
    public void addHit(HitDto hitDto) {
        try {
            statClient.sendHit(hitDto);
        } catch (Exception e) {
            throw new ServerResponseException("При отправке статистики возикла ошибка", e.getCause());
        }
    }

    @Override
    public List<StatisticDto> getStatistic(String start, String end, String[] uris, Boolean unique) {
        Map<String, String> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", Arrays.toString(uris));
        params.put("unique", unique.toString());
        return statClient.getStatisticByParams(params);
    }

}
