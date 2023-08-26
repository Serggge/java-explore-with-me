package ru.practicum.explorewithme.reaction.service;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.reaction.model.Reaction;
import ru.practicum.explorewithme.user.model.User;

public interface ReactionMapper {

    Reaction mapToReaction(User user, Event event, boolean positive);
}
