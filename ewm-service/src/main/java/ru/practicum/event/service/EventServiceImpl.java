package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.EventDateException;
import ru.practicum.exception.EventUpdateStateException;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static ru.practicum.category.service.CategoryServiceImpl.CATEGORY_NOT_FOUND_MESSAGE;
import static ru.practicum.event.mapper.EventMapper.formatter;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final static String ADMIN_STATE_ACTION_PUBLISH = "PUBLISH_EVENT";
    private final static String ADMIN_STATE_ACTION_REJECT = "REJECT_EVENT";
    public final static String EVENT_NOT_FOUND_MESSAGE = "Event with id %d was not found";
    public final static String EVENT_UPDATE_STATE_MESSAGE = "Cannot publish the event with id %d, because it's not in the right state: %s";

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

    @Override
    public EventFullDto updateAdmin(int eventId, UpdateEventAdminRequest adminEventRequest) {

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        Event oldEvent = optionalEvent.get();
        if (!oldEvent.getState().equals(EventState.PENDING)) {
            throw new EventUpdateStateException(EVENT_UPDATE_STATE_MESSAGE, eventId, oldEvent.getState().name());
        }

        LocalDateTime publishDate = LocalDateTime.now();
        Duration duration = Duration.between(publishDate, oldEvent.getEventDate());
        if (duration.toHours() < 1)
            throw new EventDateException(eventId);

        if (adminEventRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(adminEventRequest.getAnnotation());
        }
        if (adminEventRequest.getCategory() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(adminEventRequest.getCategory());
            if (optionalCategory.isEmpty())
                throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, adminEventRequest.getCategory());
            oldEvent.setCategory(optionalCategory.get());
        }
        if (adminEventRequest.getDescription() != null) {
            oldEvent.setDescription(adminEventRequest.getDescription());
        }
        if (adminEventRequest.getEventDate() != null) {
            oldEvent.setEventDate(LocalDateTime.parse(adminEventRequest.getEventDate(), formatter));
        }
        if (adminEventRequest.getLocation() != null) {
            LocationDto locationDto = adminEventRequest.getLocation();
            Optional<Location> optionalLocation = locationRepository.
                    findLocationByLatitudeAndLongitude(locationDto.getLat(), locationDto.getLon());
            Location newLocation;
            if (optionalLocation.isEmpty()) {
                newLocation = locationRepository.save(
                        Location.builder()
                                .latitude(locationDto.getLat())
                                .longitude(locationDto.getLon())
                                .build()
                );
            } else {
                newLocation = optionalLocation.get();
            }
            oldEvent.setLocation(newLocation);
        }

        if (adminEventRequest.getPaid() != null) {
            oldEvent.setPaid(adminEventRequest.getPaid());
        }

        if (adminEventRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(adminEventRequest.getParticipantLimit());
        }

        if (adminEventRequest.getRequestModeration() != null) {
            oldEvent.setRequestModeration(adminEventRequest.getRequestModeration());
        }

        if (adminEventRequest.getStateAction() != null) {
            if (adminEventRequest.getStateAction().equals(ADMIN_STATE_ACTION_PUBLISH)) {
                oldEvent.setState(EventState.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
            } else {
                oldEvent.setState(EventState.CANCELED);
            }
        }

        if (adminEventRequest.getTitle() != null) {
            oldEvent.setTitle(adminEventRequest.getTitle());
        }


        return eventMapper.mapToEventFullDto(eventRepository.save(oldEvent));

    }


}
