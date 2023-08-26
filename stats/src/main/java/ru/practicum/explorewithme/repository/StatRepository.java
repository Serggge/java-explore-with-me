package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.CountByApp;
import ru.practicum.explorewithme.model.Statistic;
import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Statistic, Long> {

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where app.uri = :uri " +
            "and app.name = :appName " +
            "group by app.name, app.uri")
    CountByApp findUniqueStatistic(String uri, String appName);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.name, app.uri, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.name, app.uri " +
            "order by count(stat.id) desc")
    List<CountByApp> findStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.name, app.uri, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.name, app.uri " +
            "order by count(stat.id) desc")
    List<CountByApp> findStatistic(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.name, app.uri " +
            "order by count(stat.ip) desc")
    List<CountByApp> findUniqueStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.name, app.uri, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.name, app.uri " +
            "order by count(stat.ip) desc")
    List<CountByApp> findUniqueStatistic(LocalDateTime start, LocalDateTime end, String[] uris);

}
