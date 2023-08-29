package ru.practicum.explorewithme.reaction.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;

public interface ReactionService {

    EventFullDto add(long eventId, long userId, boolean positive);

    void delete(long eventId, long userId, boolean positive);

}
