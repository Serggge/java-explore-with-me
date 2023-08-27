package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.model.Event;
import java.util.List;

public interface CompilationMapper {

    Compilation mapToCompilation(NewCompilationDto dto, List<Event> events);

    CompilationDto mapToDto(Compilation comp);

    List<CompilationDto> mapToDto(Iterable<Compilation> comps);

}
