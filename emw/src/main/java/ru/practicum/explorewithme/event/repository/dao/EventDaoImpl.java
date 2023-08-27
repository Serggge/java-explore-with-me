package ru.practicum.explorewithme.event.repository.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.category.model.Category_;
import ru.practicum.explorewithme.event.dto.Sorting;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.model.Event_;
import ru.practicum.explorewithme.user.model.User_;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class EventDaoImpl implements EventDao {

    @PersistenceContext
    private final EntityManager em;

    public List<Event> findEventsAdminRequest(List<Long> userIds, List<EventState> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              Integer from, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch(Event_.category, JoinType.INNER);
        root.fetch(Event_.initiator, JoinType.INNER);

        cq.select(root);

        List<Predicate> conditions = new ArrayList<>();
        if (userIds != null) {
            conditions.add(root.get(Event_.initiator).get(User_.id).in(userIds));
        }
        if (states != null) {
            conditions.add(root.get(Event_.state).in(states));
        }
        if (categories != null) {
            conditions.add(root.get(Event_.category).get(Category_.id).in(categories));
        }
        if (rangeStart != null && rangeEnd != null) {
            conditions.add(cb.between(root.get(Event_.eventDate), rangeStart, rangeEnd));
        }

        cq.where(conditions.toArray(new Predicate[0]));

        TypedQuery<Event> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    public List<Event> findEventsPublicRequest(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Sorting sort, Integer from, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        root.fetch(Event_.category, JoinType.INNER);
        root.fetch(Event_.initiator, JoinType.INNER);

        cq.select(root);

        List<Predicate> conditions = new ArrayList<>();
        conditions.add(cb.isNotNull(root.get(Event_.published)));
        if (text != null) {
            String templateForSearch = "%" + text.toUpperCase() + "%";
            Predicate textInAnnotation = cb.like(cb.upper(root.get(Event_.annotation)), templateForSearch);
            Predicate textInDescription = cb.like(cb.upper(root.get(Event_.description)), templateForSearch);
            conditions.add(cb.or(textInAnnotation, textInDescription));
        }
        if (categories != null) {
            conditions.add(root.get(Event_.category).get(Category_.id).in(categories));
        }
        if (paid != null) {
            conditions.add(cb.equal(root.get(Event_.paid), paid));
        }

        if (rangeStart == null || rangeEnd == null) {
            conditions.add(cb.greaterThan(root.get(Event_.eventDate), LocalDateTime.now()));
        } else {
            conditions.add(cb.between(root.get(Event_.eventDate), rangeStart, rangeEnd));
        }

        if (sort != null && sort.equals(Sorting.EVENT_DATE)) {
            cq = cq.where(conditions.toArray(new Predicate[0])).orderBy(cb.asc(root.get(Event_.eventDate)));
        } else {
            cq = cq.where(conditions.toArray(new Predicate[0]));
        }

        TypedQuery<Event> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }

    @Override
    public Map<Long, Long> getRequestsCount(Iterable<Long> eventsIds) {
        return em.createQuery(
                        "select event.id as eventId, count(request.id) as count " +
                                "from Request request " +
                                "join request.event event " +
                                "where event.id in :eventsIds " +
                                "group by event.id", Tuple.class)
                .setParameter("eventsIds", eventsIds)
                .getResultStream()
                .collect(Collectors.toMap(
                        tuple -> ((Number) tuple.get("eventId")).longValue(),
                        tuple -> ((Number) tuple.get("count")).longValue()
                ));
    }
}
