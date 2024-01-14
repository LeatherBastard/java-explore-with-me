package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.event.service.EventServiceImpl.EVENT_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;


    @Override
    public CompilationDto add(NewCompilationDto compilationDto) {
        List<Event> events = new ArrayList<>();
        for (Integer eventId : compilationDto.getEvents()) {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            if (optionalEvent.isEmpty())
                throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
            events.add(optionalEvent.get());
        }
        Compilation compilation = compilationMapper.mapToCompilation(compilationDto);
        compilation.getEvents().addAll(events);
        CompilationDto result = compilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
        result.setEvents(events.stream().map(eventMapper::mapToEventShortDto).collect(Collectors.toList()));
        return result;
    }
}
