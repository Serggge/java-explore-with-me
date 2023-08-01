package ru.practicum.explorewithme.compilation.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import ru.practicum.explorewithme.event.model.Event;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Compilation.class)
public abstract class Compilation_ {

	public static volatile SingularAttribute<Compilation, Boolean> pinned;
	public static volatile SingularAttribute<Compilation, Long> id;
	public static volatile SingularAttribute<Compilation, String> title;
	public static volatile SetAttribute<Compilation, Event> events;

	public static final String PINNED = "pinned";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String EVENTS = "events";

}

