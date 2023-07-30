package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.CountByApp;
import ru.practicum.explorewithme.model.Statistic;
import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Statistic, Long> {

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.uri, app.name, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.uri, app.name " +
            "order by count(stat.id) desc")
    List<CountByApp> findStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.uri, app.name, count(stat.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.uri, app.name " +
            "order by count(stat.id) desc")
    List<CountByApp> findStatistic(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.uri, app.name, count(distinct app.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by app.uri, app.name " +
            "order by count(stat.id) desc")
    List<CountByApp> findUniqueStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.CountByApp(app.uri, app.name, count(distinct app.id)) " +
            "from Statistic stat " +
            "join stat.app app " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and app.uri in :uris " +
            "group by app.uri, app.name " +
            "order by count(stat.id) desc")
    List<CountByApp> findUniqueStatistic(LocalDateTime start, LocalDateTime end, String[] uris);

}
