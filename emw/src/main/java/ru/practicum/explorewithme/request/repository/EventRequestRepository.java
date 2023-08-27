package ru.practicum.explorewithme.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.request.model.Request;
import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long requesterId);

    List<Request> findAllByEventInitiatorId(long initiatorId);

    @Query("select count(request.id) " +
            "from Request request " +
            "join request.event event " +
            "where event.id = :eventId " +
            "and request.status = 'CONFIRMED'")
    Long getConfirmedCount(long eventId);

    @Query("select req " +
            "from Request req " +
            "where req.id in :ids")
    List<Request> findByIds(Iterable<Long> ids);

    Optional<Request> findByEventIdAndRequesterId(long eventId, long userId);

}
