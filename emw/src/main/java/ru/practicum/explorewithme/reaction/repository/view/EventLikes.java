package ru.practicum.explorewithme.reaction.repository.view;

import lombok.Getter;
import ru.practicum.explorewithme.event.model.Event;

@Getter
public class EventLikes {

    private final Event event;
    private final Long likes;

    public EventLikes(Event event, Long likes) {
        this.event = event;
        this.likes = likes;
    }
}
