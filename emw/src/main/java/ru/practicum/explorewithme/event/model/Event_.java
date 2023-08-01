package ru.practicum.explorewithme.event.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.user.model.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Event.class)
public abstract class Event_ {

	public static volatile SingularAttribute<Event, String> annotation;
	public static volatile SingularAttribute<Event, LocalDateTime> created;
	public static volatile SingularAttribute<Event, User> initiator;
	public static volatile SingularAttribute<Event, String> description;
	public static volatile SingularAttribute<Event, LocalDateTime> published;
	public static volatile SingularAttribute<Event, String> title;
	public static volatile SingularAttribute<Event, Long> participantLimit;
	public static volatile SingularAttribute<Event, EventState> state;
	public static volatile SingularAttribute<Event, Boolean> paid;
	public static volatile SingularAttribute<Event, Boolean> requestModeration;
	public static volatile SingularAttribute<Event, Double> lat;
	public static volatile SingularAttribute<Event, Double> lon;
	public static volatile SingularAttribute<Event, Long> id;
	public static volatile SingularAttribute<Event, Category> category;
	public static volatile SingularAttribute<Event, LocalDateTime> eventDate;

	public static final String ANNOTATION = "annotation";
	public static final String CREATED = "created";
	public static final String INITIATOR = "initiator";
	public static final String DESCRIPTION = "description";
	public static final String PUBLISHED = "published";
	public static final String TITLE = "title";
	public static final String PARTICIPANT_LIMIT = "participantLimit";
	public static final String STATE = "state";
	public static final String PAID = "paid";
	public static final String REQUEST_MODERATION = "requestModeration";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String ID = "id";
	public static final String CATEGORY = "category";
	public static final String EVENT_DATE = "eventDate";

}

