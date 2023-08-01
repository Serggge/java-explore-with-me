package ru.practicum.explorewithme.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.service.CompilationMapper;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@Component
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Setter
public class CompilationMapperImpl implements CompilationMapper {

    private final EventMapper eventMapper;

    @Override
    public Compilation mapToCompilation(NewCompilationDto dto, List<Event> events) {
        Compilation comp = new Compilation();
        comp.setTitle(dto.getTitle());
        if (dto.getPinned() == null || dto.getPinned() == Boolean.FALSE) {
            comp.setPinned(Boolean.FALSE);
        } else {
            comp.setPinned(Boolean.TRUE);
        }
        comp.setEvents(new HashSet<>(events));
        return comp;
    }

    @Override
    public CompilationDto mapToDto(Compilation comp) {
        CompilationDto dto = new CompilationDto();
        dto.setId(comp.getId());
        dto.setTitle(comp.getTitle());
        dto.setPinned(comp.getPinned());
        TreeSet<EventShortDto> eventsShort = new TreeSet<>(Comparator.comparingLong(EventShortDto::getId));
        eventsShort.addAll(eventMapper.mapToShortDto(comp.getEvents()));
        dto.setEvents(eventsShort);
        return dto;
    }

    @Override
    public List<CompilationDto> mapToDto(Iterable<Compilation> comps) {
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation comp : comps) {
            result.add(mapToDto(comp));
        }
        return result;
    }
}
