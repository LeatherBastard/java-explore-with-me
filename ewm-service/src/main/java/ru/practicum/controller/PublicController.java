package ru.practicum.controller;


import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    private static final String LOGGER_GET_CATEGORIES_MESSAGE = "Returning list of categories";

    private static final String LOGGER_GET_CATEGORY_BY_ID_MESSAGE = "Getting category with id: {}";

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategories(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_CATEGORIES_MESSAGE);
        return categoryService.findAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable("catId") int catId) {
        log.info(LOGGER_GET_CATEGORY_BY_ID_MESSAGE, catId);
        return categoryService.getById(catId);
    }
}
