package ru.practicum.explorewithme.statistic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.exception.clientServer.ServerResponseException;
import ru.practicum.explorewithme.service.StatClient;
import ru.practicum.explorewithme.service.StatsClientFactory;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static ru.practicum.explorewithme.util.Constants.DATE_FORMAT;

@Service
public class StatProxyServiceImpl implements StatProxyService {

    private final StatClient statClient;
    private final String appName;

    public StatProxyServiceImpl(@Value("${explore.stats-server.url}") String serviceUrl,
                                @Value("${app.name}") String appName) {
        this.statClient = StatsClientFactory.getClient(serviceUrl);
        this.appName = appName;
    }

    @Override
    public ViewStats addHit(HttpServletRequest request) {
        EndpointHit hitDto = EndpointHit
                .builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        try {
            return statClient.sendHit(hitDto);
        } catch (Exception e) {
            throw new ServerResponseException("При отправке статистики возикла ошибка", e.getCause());
        }
    }

    @Override
    public List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        Map<String, String> params = new HashMap<>();
        params.put("start", start.format(DATE_FORMAT));
        params.put("end", end.format(DATE_FORMAT));
        params.put("uris", Arrays.toString(uris));
        params.put("unique", unique.toString());
        return statClient.getStatisticByParams(params);
    }

}
