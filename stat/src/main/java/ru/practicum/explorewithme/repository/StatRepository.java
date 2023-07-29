package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.StatByUri;
import ru.practicum.explorewithme.model.Statistic;
import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Statistic, Long> {

    @Query("select new ru.practicum.explorewithme.model.StatByUri(stat.uri, stat.app, count(stat.id)) " +
            "from Statistic stat " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by stat.uri " +
            "order by count(stat.id) desc")
    List<StatByUri> findStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.StatByUri(stat.uri, stat.app, count(stat.id)) " +
            "from Statistic stat " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and stat.uri in :uris " +
            "group by stat.uri " +
            "order by count(stat.id) desc")
    List<StatByUri> findStatistic(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.explorewithme.model.StatByUri(stat.uri, stat.app, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "group by stat.uri " +
            "order by count(stat.id) desc")
    List<StatByUri> findUniqueStatistic(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.explorewithme.model.StatByUri(stat.uri, stat.app, count(distinct stat.ip)) " +
            "from Statistic stat " +
            "where stat.timestamp > :start " +
            "and stat.timestamp < :end " +
            "and stat.uri in :uris " +
            "group by stat.uri " +
            "order by count(stat.id) desc")
    List<StatByUri> findUniqueStatistic(LocalDateTime start, LocalDateTime end, String[] uris);
}
