package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.CompilationEventsNotFoundException;
import ru.practicum.exception.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    public static final String COMPILATION_NOT_FOUND_MESSAGE = "Compilation with id %d not found";
    public static final String EVENT_COMPILATION_NOT_FOUND_MESSAGE = "One of the events from compilation, was not found";
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;
    private final EntityManager entityManager;


    @Override
    public CompilationDto add(NewCompilationDto compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllById(compilationDto.getEvents());
            if (events.size() != compilationDto.getEvents().size())
                throw new CompilationEventsNotFoundException(compilationDto.getEvents());
        }


        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        Compilation compilation = compilationMapper.mapToCompilation(compilationDto);
        compilation.setEvents(events);
        CompilationDto result = compilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
        result.setEvents(events.stream().map(eventMapper::mapToEventShortDto).collect(Collectors.toList()));
        return result;
    }

    @Override
    public CompilationDto getById(int compId) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compId);
        if (optionalCompilation.isEmpty())
            throw new EntityNotFoundException(COMPILATION_NOT_FOUND_MESSAGE, compId);
        Compilation compilation = optionalCompilation.get();
        CompilationDto result = compilationMapper.mapToCompilationDto(optionalCompilation.get());
        result.setEvents(compilation.getEvents().stream().map(eventMapper::mapToEventShortDto).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<CompilationDto> findAllCompilations(Boolean pinned, int from, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compilation> criteriaQuery = criteriaBuilder.createQuery(Compilation.class);
        Root<Compilation> root = criteriaQuery.from(Compilation.class);
        List<Predicate> predicates = new ArrayList<>();
        if (pinned != null) {
            if (pinned) {
                predicates.add(criteriaBuilder.isTrue(root.get("pinned")));
            } else {
                predicates.add(criteriaBuilder.isFalse(root.get("pinned")));
            }
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Compilation> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);

        List<Compilation> compilations = query.getResultList();
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationDto compilationDto = compilationMapper.mapToCompilationDto(compilation);
            compilationDto.setEvents(compilation.getEvents().stream().map(eventMapper::mapToEventShortDto).collect(Collectors.toList()));
            result.add(compilationDto);
        }
        return result;
    }


    @Override
    public void remove(int compId) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compId);
        if (optionalCompilation.isEmpty())
            throw new EntityNotFoundException(COMPILATION_NOT_FOUND_MESSAGE, compId);
        compilationRepository.delete(optionalCompilation.get());
    }

    @Override
    public CompilationDto update(int compId, UpdateCompilationRequest compilationDto) {
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compId);
        if (optionalCompilation.isEmpty())
            throw new EntityNotFoundException(COMPILATION_NOT_FOUND_MESSAGE, compId);
        Compilation oldCompilation = optionalCompilation.get();
        if (compilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
            if (events.size() != compilationDto.getEvents().size())
                throw new CompilationEventsNotFoundException(compilationDto.getEvents());

            oldCompilation.setEvents(events);
        }


        if (compilationDto.getPinned() != null) {
            oldCompilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            oldCompilation.setTitle(compilationDto.getTitle());
        }

        CompilationDto result = compilationMapper.mapToCompilationDto(compilationRepository.save(oldCompilation));
        result.setEvents(oldCompilation.getEvents().stream().map(eventMapper::mapToEventShortDto).collect(Collectors.toList()));
        return result;
    }

}
