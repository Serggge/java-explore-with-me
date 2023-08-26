package ru.practicum.explorewithme.reaction.service.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.reaction.service.ReactionMapper;
import ru.practicum.explorewithme.user.model.User;
import java.time.LocalDateTime;

@Component
public class ReactionMapperImpl implements ReactionMapper {

    @Override
    public Reaction mapToReaction(User user, Event event, boolean positive) {
        Reaction reaction = new Reaction();
        reaction.setUser(user);
        reaction.setEvent(event);
        reaction.setPositive(positive);
        reaction.setTimestamp(LocalDateTime.now());
        return reaction;
    }
}
