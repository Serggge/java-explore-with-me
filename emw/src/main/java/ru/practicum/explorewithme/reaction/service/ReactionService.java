package ru.practicum.explorewithme.reaction.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.reaction.dto.CategoriesDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import java.util.List;

public interface ReactionService {

    EventFullDto add(long eventId, long userId, boolean positive);

    void delete(long eventId, long userId, boolean positive);

    List<EventShortDto> getPopularEvents(Long categoryId, int from, int size);

    List<EventShortDto> getPopularEvents(CategoriesDto catDto, int from, int size);

    List<EventShortDto> getPopularByPartName(String name, String type, int from, int size);

    List<UserShortDto> getPopularInitiators(int from, int size);

    Event fillPopularity(Event event);

    List<Event> fillPopularity(Iterable<Event> events);
}
