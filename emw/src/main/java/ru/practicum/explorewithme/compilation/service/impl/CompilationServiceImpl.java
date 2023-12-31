package ru.practicum.explorewithme.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.compilation.service.CompilationMapper;
import ru.practicum.explorewithme.compilation.service.CompilationService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.notFound.CompilationNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventService eventService;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        log.debug("Getting all Compilations by params. Pinned={}, from={}, size={}", pinned, from, size);
        Pageable page = PageRequest.of(from, size);
        Page<Compilation> comps;
        if (pinned == null || pinned == Boolean.FALSE) {
            comps = compilationRepository.findAll(page);
        } else {
            comps = compilationRepository.findAllByPinned(pinned, page);
        }
        return compilationMapper.mapToDto(comps);
    }

    @Override
    public CompilationDto getById(long compId) {
        log.debug("Getting compilation by ID={}", compId);
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(String.format("Compilation with ID=%d not found", compId)));
        return compilationMapper.mapToDto(comp);
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto dto) {
        List<Event> events;
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            events = eventService.getEvents(dto.getEvents());
        } else {
            events = new ArrayList<>();
        }
        Compilation comp = compilationMapper.mapToCompilation(dto, events);
        comp = compilationRepository.save(comp);
        log.info("Compilation created: {}", comp);
        return compilationMapper.mapToDto(comp);
    }

    @Override
    public CompilationDto update(long compId, UpdateCompilationRequest dto) {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(String.format("Category with id=%d was not found", compId)));
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            comp.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            comp.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            List<Event> events = eventService.getEvents(dto.getEvents());
            comp.setEvents(new HashSet<>(events));
        } else {
            comp.setEvents(new HashSet<>());
        }
        comp = compilationRepository.save(comp);
        log.info("Compilation updated: {}", comp);
        return compilationMapper.mapToDto(comp);
    }

    @Override
    public void delete(long compId) {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException(String.format("Category with id=%d was not found", compId)));
        compilationRepository.delete(comp);
        log.info("Compilation deleted: {}", comp);
    }
}
