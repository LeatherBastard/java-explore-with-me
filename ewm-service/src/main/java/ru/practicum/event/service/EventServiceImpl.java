package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.practicum.category.service.CategoryServiceImpl.CATEGORY_NOT_FOUND_MESSAGE;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    public final static String EVENT_NOT_FOUND_MESSAGE = "Event with id %d was not found";

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;
    private final EventMapper eventMapper;


    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {
        Optional<User> optionalInitiator = userRepository.findById(userId);
        if (optionalInitiator.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Category> optionalCategory = categoryRepository.findById(eventDto.getCategory());
        if (optionalCategory.isEmpty())
            throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, userId);
        Optional<Location> optionalLocation = locationRepository
                .findLocationByLatitudeAndLongitude(
                        eventDto.getLocation().getLat(),
                        eventDto.getLocation().getLon()
                );
        Location location;
        if (optionalLocation.isEmpty())
            location = locationRepository.save(locationMapper.mapToLocation(eventDto.getLocation()));
        else
            location = optionalLocation.get();
        Event event = eventMapper.mapToEvent(eventDto);
        event.setInitiator(optionalInitiator.get());
        event.setCategory(optionalCategory.get());
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        if (event.getRequestModeration()) {
            event.setState(EventState.PENDING);
        } else {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        event.setConfirmedRequests(0);
        event.setViews(0);
        return eventMapper.mapToEventFullDto(eventRepository.save(event));
    }
}
