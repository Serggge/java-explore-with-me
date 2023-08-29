package ru.practicum.explorewithme.reaction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.repository.view.EventLikes;
import ru.practicum.explorewithme.reaction.repository.view.ReactionView;
import ru.practicum.explorewithme.reaction.repository.view.UserLikes;
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

    @Query("select new ru.practicum.explorewithme.reaction.repository.view.EventLikes(event, count(re.positive)) " +
            "from Reaction re " +
            "join re.event event " +
            "join event.category cat " +
            "where cat.id = :categoryId " +
            "and re.positive = true " +
            "group by event.id " +
            "order by count(re.positive) desc")
    List<EventLikes> findReactionsByCategory(long categoryId, Pageable page);

    @Query("select new ru.practicum.explorewithme.reaction.repository.view.EventLikes(event, count(re.positive)) " +
            "from Reaction re " +
            "join re.event event " +
            "where lower(event.annotation) like lower(concat('%', :text,'%')) " +
            "or lower(event.description) like lower(concat('%', :text,'%')) " +
            "group by event.id " +
            "order by count(re.positive) desc")
    List<EventLikes> findPopularEvents(String text, Pageable page);

    @Query("select new ru.practicum.explorewithme.reaction.repository.view.EventLikes(event, count(re.positive)) " +
            "from Reaction re " +
            "join re.event event " +
            "join event.category cat " +
            "where re.positive = true " +
            "and (cat.id in (:catIds) " +
            "or cat.name in (:categoryNames)) " +
            "group by event.id " +
            "order by count(re.positive) desc")
    List<EventLikes> findPopularEvents(Iterable<Long> catIds, Set<String> categoryNames, Pageable page);

    @Query("select new ru.practicum.explorewithme.reaction.repository.view.UserLikes(init, count(react.positive)) " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.initiator init " +
            "where react.positive = true " +
            "group by init.id " +
            "order by count(react.positive) desc")
    List<UserLikes> findPopularInitiators(Pageable page);

    Optional<Reaction> findByEventIdAndUserId(long eventId, long userId);

}
