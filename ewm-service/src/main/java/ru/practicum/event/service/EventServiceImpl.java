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

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final static String INITIATOR_NOT_FOUND_MESSAGE = "Initiator with id %d was not found";
    private final static String CATEGORY_NOT_FOUND_MESSAGE = "Category with id %d was not found";

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;
    private final EventMapper eventMapper;


    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {
        //@TODO
        // Не парсит в NewEventDto строку с датой и временем в LocalDateTime
        //Кроме того, вместо категории передаётся её айди
        // смотри, как делаются запросы в свагере, там даже есть тип данный особенно в New event DTO

        Optional<User> optionalInitiator = userRepository.findById(userId);
        if (optionalInitiator.isEmpty())
            throw new EntityNotFoundException(INITIATOR_NOT_FOUND_MESSAGE, userId);
        Optional<Category> optionalCategory = categoryRepository.findById(eventDto.getCategory());
        if (optionalCategory.isEmpty())
            throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, userId);
        Optional<Location> optionalLocation = locationRepository
                .findLocationByLatitudeAndLongitude(
                        eventDto.getLocation().getLatitude(),
                        eventDto.getLocation().getLongitude()
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
        if (event.getRequestModeration()) {
            event.setState(EventState.PENDING);
        } else {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());

        }
        return eventMapper.mapToEventFullDto(eventRepository.save(event));
    }
}
