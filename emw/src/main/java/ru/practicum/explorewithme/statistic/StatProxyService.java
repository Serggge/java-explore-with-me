package ru.practicum.explorewithme.statistic;

import ru.practicum.explorewithme.dto.StatisticDto;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface StatProxyService {

    StatisticDto addHit(HttpServletRequest request);

    List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

}
