package ru.practicum.explorewithme.statistic;

import ru.practicum.explorewithme.dto.ViewStats;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatProxyService {

    ViewStats addHit(HttpServletRequest request);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

}
