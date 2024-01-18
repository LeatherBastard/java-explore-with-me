package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private static final String LOGGER_ADD_USER_MESSAGE = "Adding user ";
    private static final String LOGGER_GET_USERS_MESSAGE = "Returning list of users";
    private static final String LOGGER_REMOVE_USER_MESSAGE = "Removing user with id: {}";

    private static final String LOGGER_ADD_CATEGORY_MESSAGE = "Adding category";
    private static final String LOGGER_REMOVE_CATEGORY_MESSAGE = "Removing category with id: {}";
    private static final String LOGGER_UPDATE_CATEGORY_MESSAGE = "Updating category with id: {}";

    private static final String LOGGER_UPDATE_ADMIN_EVENT_MESSAGE = "Updating event from admin with event id: {}";
    private static final String LOGGER_GET_EVENTS_MESSAGE = "Returning list of events for admin";

    private static final String LOGGER_ADD_COMPILATION_MESSAGE = "Adding compilation";
    private static final String LOGGER_REMOVE_COMPILATION_MESSAGE = "Removing compilation with id: {}";
    private static final String LOGGER_UPDATE_COMPILATION_MESSAGE = "Updating compilation with id: {}";


    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Validated UserDto user) {
        log.info(LOGGER_ADD_USER_MESSAGE);
        return userService.addUser(user);
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsersByIds(@RequestParam(required = false) List<Integer> ids, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_USERS_MESSAGE);
        return userService.findAllByIds(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") int userId) {
        log.info(LOGGER_REMOVE_USER_MESSAGE, userId);
        userService.remove(userId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Validated CategoryDto category) {
        log.info(LOGGER_ADD_CATEGORY_MESSAGE);
        return categoryService.add(category);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") int catId) {
        log.info(LOGGER_REMOVE_CATEGORY_MESSAGE, catId);
        categoryService.remove(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable("catId") int catId, @RequestBody CategoryDto category) {
        log.info(LOGGER_UPDATE_CATEGORY_MESSAGE, catId);
        return categoryService.update(catId, category);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") int eventId, @RequestBody @Validated UpdateEventAdminRequest adminEventRequest) {
        log.info(LOGGER_UPDATE_ADMIN_EVENT_MESSAGE, eventId);
        return eventService.updateEventByAdmin(eventId, adminEventRequest);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_EVENTS_MESSAGE);
        return eventService.findAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }


    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Validated NewCompilationDto compilation) {
        log.info(LOGGER_ADD_COMPILATION_MESSAGE);
        return compilationService.add(compilation);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") int compId) {
        log.info(LOGGER_REMOVE_COMPILATION_MESSAGE, compId);
        compilationService.remove(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") int compId, @RequestBody @Validated UpdateCompilationRequest compilation) {
        log.info(LOGGER_UPDATE_COMPILATION_MESSAGE, compId);
        return compilationService.update(compId, compilation);
    }

    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@RequestParam(required = false) List<Integer> users,
                                                @RequestParam(required = false) List<Integer> events,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info(LOGGER_GET_COMMENTS_MESSAGE);
        return commentService.findAllCommentsByAdmin(users, events, states, rangeStart, rangeEnd, from, size);
    }

}
