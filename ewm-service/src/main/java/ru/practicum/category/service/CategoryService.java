package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(CategoryDto category);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto getById(int catId);

    void remove(int id);

    CategoryDto update(int catId, CategoryDto category);
}
