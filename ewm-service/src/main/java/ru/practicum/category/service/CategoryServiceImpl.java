package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.CategoryNameOccupiedException;
import ru.practicum.exception.CategoryRelatedEventsException;
import ru.practicum.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category with id %d not found";


    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto add(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = repository.findByName(categoryDto.getName());
        if (optionalCategory.isPresent())
            throw new CategoryNameOccupiedException(categoryDto.getName());
        return mapper.mapToCategoryDto(repository.save(mapper.mapToCategory(categoryDto)));
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        return repository.findAll(from, size).stream().map(mapper::mapToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(int catId) {
        return mapper.mapToCategoryDto(
                repository.findById(catId)
                        .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, catId))
        );
    }

    @Override
    public void remove(int catId) {
        Category category = mapper.mapToCategory(getById(catId));
        List<Event> events = eventRepository.findByCategory_Id(catId);
        if (events.size() > 0)
            throw new CategoryRelatedEventsException(catId);
        repository.delete(category);
    }

    @Override
    public CategoryDto update(int catId, CategoryDto category) {
        Optional<Category> optionalCategory = repository.findByName(category.getName());
        if (optionalCategory.isPresent() && optionalCategory.get().getId() != catId)
            throw new CategoryNameOccupiedException(category.getName());
        Category oldCategory = mapper.mapToCategory(getById(catId));
        return mapper.mapToCategoryDto(repository.save(mapper.mapToCategory(category)));
    }


}
