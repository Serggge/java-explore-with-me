package ru.practicum.explorewithme.statistic;

import ru.practicum.explorewithme.dto.ViewStats;
import java.time.LocalDateTime;
import java.util.List;

public interface StatProxyService {

    ViewStats addHit(String appName, String uri, String ip);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

}
