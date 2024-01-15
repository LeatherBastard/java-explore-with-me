package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto add(NewCompilationDto compilationDto);

    CompilationDto getById(int compId);

    List<CompilationDto> findAllCompilations(Boolean pinned, int from, int size);

    void remove(int compId);

    CompilationDto update(int compId, NewCompilationDto compilationDto);
}
