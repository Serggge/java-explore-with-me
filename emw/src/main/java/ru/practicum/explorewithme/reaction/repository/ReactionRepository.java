package ru.practicum.explorewithme.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.reaction.model.Popularity;
import ru.practicum.explorewithme.reaction.model.Reaction;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("select new ru.practicum.explorewithme.reaction.model.Popularity(" +
            "event.id) " +
            "from Reaction reaction " +
            "join reaction.event event " +
            "where event.id = :eventId " +
            "group by event.id")
    Optional<Popularity> getPopularity(long eventId);

    Optional<Reaction> findByEventIdAndUserId(long eventId, long userId);

}
