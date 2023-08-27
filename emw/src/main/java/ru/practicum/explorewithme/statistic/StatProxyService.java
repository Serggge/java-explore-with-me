package ru.practicum.explorewithme.statistic;

import ru.practicum.explorewithme.dto.ViewStats;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatProxyService {

    ViewStats addHit(String appName, String uri, String ip);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique);

}
