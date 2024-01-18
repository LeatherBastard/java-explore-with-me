package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.client.statistic.StatisticHttpClient;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;
import ru.practicum.statistic.dto.StatisticRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.event.mapper.EventMapper.formatter;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class PublicController {
    private static final String LOGGER_GET_CATEGORIES_MESSAGE = "Returning list of categories";
    private static final String LOGGER_GET_EVENTS_MESSAGE = "Returning list of events";
    private static final String LOGGER_GET_COMPILATIONS_MESSAGE = "Returning list of compilations";
    private static final String LOGGER_GET_CATEGORY_BY_ID_MESSAGE = "Getting category with id: {}";
    private static final String LOGGER_GET_EVENT_BY_ID_MESSAGE = "Getting event with id: {}";
    private static final String LOGGER_GET_COMPILATION_BY_ID_MESSAGE = "Getting compilation with id: {}";

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;
    private final StatisticHttpClient statisticHttpClient;

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
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info(LOGGER_GET_EVENTS_MESSAGE);

        List<EventFullDto> events = eventService.findAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statisticHttpClient.addHit(StatisticRequestDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        return events;
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable("id") int id, HttpServletRequest request) {
        log.info(LOGGER_GET_EVENT_BY_ID_MESSAGE, id);
        EventFullDto event = eventService.getById(id, request);
        statisticHttpClient.addHit(StatisticRequestDto
                .builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        return event;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_COMPILATIONS_MESSAGE);
        return compilationService.findAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") int compId) {
        log.info(LOGGER_GET_COMPILATION_BY_ID_MESSAGE, compId);
        return compilationService.getById(compId);
    }

    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_COMMENTS_MESSAGE);
        return commentService.findAllComments(text, rangeStart, rangeEnd, from, size);
    }


}
