package ru.practicum.explorewithme.reaction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.user.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("select ev.id as eventId, count(react.positive) as likes " +
            "from Reaction react " +
            "join react.event ev " +
            "where ev.id = :eventId " +
            "and react.positive = true " +
            "group by ev.id")
    Optional<Likes> findEventLikes(long eventId);

    @Query("select ev.id as eventId, count(react.positive) as likes " +
            "from Reaction react " +
            "join react.event ev " +
            "where ev.id in :eventIds " +
            "and react.positive = true " +
            "group by ev.id")
    List<Likes> findEventLikes(Iterable<Long> eventIds);

    @Query("select ev.id as eventId, count(react.positive) as dislikes " +
            "from Reaction react " +
            "join react.event ev " +
            "where ev.id = :eventId " +
            "and react.positive = false " +
            "group by ev.id")
    Optional<Dislikes> findEventDislikes(long eventId);

    @Query("select ev.id as eventId, count(react.positive) as dislikes " +
            "from Reaction react " +
            "join react.event ev " +
            "where ev.id in :eventIds " +
            "and react.positive = false " +
            "group by ev.id")
    List<Dislikes> findEventDislikes(Iterable<Long> eventIds);

    @Query("select ev " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.category cat " +
            "where cat.id = :categoryId " +
            "group by ev.id " +
            "order by count(react.positive) desc")
    List<Event> findPopularEvents(long categoryId, Pageable page);

    @Query("select ev " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.category cat " +
            "where cat.name in :categoryNames " +
            "group by ev.id " +
            "order by count(react.positive) desc")
    List<Event> findPopularEvents(Set<String> categoryNames, Pageable page);

    @Query("select ev " +
            "from Reaction react " +
            "join react.event ev " +
            "where lower(ev.annotation) like lower(concat('%', :text,'%')) " +
            "or lower(ev.description) like lower(concat('%', :text,'%')) " +
            "group by ev.id " +
            "order by count(react.positive) desc")
    List<Event> findPopularEvents(String text, Pageable page);

    @Query("select ev " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.category cat " +
            "where cat.id in :catIds " +
            "group by ev.id " +
            "order by count(react.positive) desc")
    List<Event> findPopularEvents(Iterable<Long> catIds, Pageable page);

    @Query("select init " +
            "from Reaction react " +
            "join react.event ev " +
            "join ev.initiator init " +
            "where react.positive = true " +
            "group by init.id " +
            "order by count(react.positive) desc")
    List<User> findPopularInitiators(Pageable page);

    Optional<Reaction> findByEventIdAndUserId(long eventId, long userId);

}
