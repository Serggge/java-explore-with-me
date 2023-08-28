package ru.practicum.explorewithme.reaction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.repository.view.ReactionView;
import ru.practicum.explorewithme.reaction.repository.view.LikesView;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query(value = "with cte as (select positive pos, count(positive) count " +
            "from reactions where event_id = :eventId group by positive) " +
            "select :eventId eventId, " +
            "(select cte.count from cte where cte.pos = true) likes, " +
            "(select cte.count from cte where cte.pos = false) dislikes " +
            "from cte group by eventId",
            nativeQuery = true)
    Optional<ReactionView> findReactionInfo(long eventId);

    @Query("select ev.id as id, count(re.positive) as count " +
            "from Reaction re " +
            "join re.event ev " +
            "join ev.category cat " +
            "where cat.id = :categoryId " +
            "and re.positive = true " +
            "group by ev.id " +
            "order by count(re.positive) desc")
    List<LikesView> findReactionsByCategory(long categoryId, Pageable page);

    @Query("select ev.id as id, count(re.positive) as count " +
            "from Reaction re " +
            "join re.event ev " +
            "where lower(ev.annotation) like lower(concat('%', :text,'%')) " +
            "or lower(ev.description) like lower(concat('%', :text,'%')) " +
            "group by ev.id " +
            "order by count(re.positive) desc")
    List<LikesView> findPopularEvents(String text, Pageable page);

    @Query("select ev.id as id, count(re.positive) as count " +
            "from Reaction re " +
            "join re.event ev " +
            "join ev.category cat " +
            "where re.positive = true " +
            "and (cat.id in (:catIds) " +
            "or cat.name in (:categoryNames)) " +
            "group by ev.id " +
            "order by count(re.positive) desc")
    List<LikesView> findPopularEvents(Iterable<Long> catIds, Set<String> categoryNames, Pageable page);

    @Query("select init.id as id, count(react.positive) as count " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.initiator init " +
            "where react.positive = true " +
            "group by init.id " +
            "order by count(react.positive) desc")
    List<LikesView> findPopularInitiators(Pageable page);

    Optional<Reaction> findByEventIdAndUserId(long eventId, long userId);

}
