package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.dao.EventDao;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventDao {

    Optional<Event> findByIdAndPublishedNotNull(Long eventId);

    Page<Event> findAllByInitiatorId(long userId, Pageable page);

    @Query("select event " +
            "from Event event " +
            "where event.id in :ids " +
            "order by event.id")
    List<Event> findAllByIds(Iterable<Long> ids);

    @Query("select count(event.id) " +
            "from Event event " +
            "join event.category cat " +
            "where cat.id = :catId")
    Long getCountEventsByCategory(long catId);

}
