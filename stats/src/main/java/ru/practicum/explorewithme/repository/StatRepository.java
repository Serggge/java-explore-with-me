package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.model.Statistic;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatRepository extends JpaRepository<Statistic, Long> {

    @Query("select new ru.practicum.explorewithme.dto.ViewStats(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where app.uri = :uri " +
            "and app.name = :appName " +
            "group by app.name, app.uri")
    Optional<ViewStats> findUniqueStatistic(String uri, String appName);

    @Query("select new ru.practicum.explorewithme.dto.ViewStats(app.name, app.uri, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.name, app.uri " +
            "order by count(stat.id) desc")
    List<ViewStats> findStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.dto.ViewStats(app.name, app.uri, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.name, app.uri " +
            "order by count(stat.id) desc")
    List<ViewStats> findStatistic(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.explorewithme.dto.ViewStats(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.name, app.uri " +
            "order by count(stat.ip) desc")
    List<ViewStats> findUniqueStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.dto.ViewStats(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.name, app.uri " +
            "order by count(stat.ip) desc")
    List<ViewStats> findUniqueStatistic(LocalDateTime start, LocalDateTime end, List<String> uris);

}
