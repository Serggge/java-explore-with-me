package ru.practicum.explorewithme.reaction.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.reaction.repository.view.Reactions;

import java.util.Map;

public interface ReactionService {

    EventFullDto add(long eventId, long userId, boolean positive);

    void delete(long eventId, long userId, boolean positive);

    Map<Long, Long> getPopularEvents(Long categoryId, int from, int size);

    Map<Long, Long> getPopularEvents(CategoriesDto catDto, int from, int size);

    Map<Long, Long> getPopularByPartName(String text, int from, int size);

    Map<Long, Long> getPopularInitiators(int from, int size);

    Reactions getReactions(long eventId);

}
