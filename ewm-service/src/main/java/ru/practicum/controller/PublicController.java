package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    private static final String LOGGER_GET_CATEGORIES_MESSAGE = "Returning list of categories";
    private static final String LOGGER_GET_EVENTS_MESSAGE = "Returning list of events";
    private static final String LOGGER_GET_CATEGORY_BY_ID_MESSAGE = "Getting category with id: {}";

    private static final String LOGGER_GET_EVENT_BY_ID_MESSAGE = "Getting event with id: {}";

    private final CategoryService categoryService;
    private final EventService eventService;

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


    @GetMapping("/events")
    public List<EventFullDto> findAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_EVENTS_MESSAGE);
        return eventService.findAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable("id") int id) {
        log.info(LOGGER_GET_EVENT_BY_ID_MESSAGE, id);
        return eventService.getById(id);
    }

}
