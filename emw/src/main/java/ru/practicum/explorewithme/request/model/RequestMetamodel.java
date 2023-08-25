package ru.practicum.explorewithme.request.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.model.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Request.class)
public abstract class RequestMetamodel {

	public static volatile SingularAttribute<Request, User> requester;
	public static volatile SingularAttribute<Request, LocalDateTime> created;
	public static volatile SingularAttribute<Request, Long> id;
	public static volatile SingularAttribute<Request, Event> event;
	public static volatile SingularAttribute<Request, Status> status;

	public static final String REQUESTER = "requester";
	public static final String CREATED = "created";
	public static final String ID = "id";
	public static final String EVENT = "event";
	public static final String STATUS = "status";

}

