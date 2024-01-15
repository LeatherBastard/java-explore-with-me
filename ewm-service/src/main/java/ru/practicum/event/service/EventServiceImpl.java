package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.AdminStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UserStateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.*;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.category.service.CategoryServiceImpl.CATEGORY_NOT_FOUND_MESSAGE;
import static ru.practicum.event.mapper.EventMapper.formatter;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    public final static String EVENT_NOT_FOUND_MESSAGE = "Event with id %d was not found";
    public final static String EVENT_ADMIN_UPDATE_DATE_MESSAGE = "Cannot publish event with id %d," +
            "because event date has to be not earlier than hour before publication";

    public final static String EVENT_USER_UPDATE_DATE_MESSAGE = "Cannot update event with id %d," +
            "because event date has to be not earlier than two hours before publication";

    public final static String EVENT_DATE_BEFORE_CURRENT_MESSAGE = "Cannot publish event with id %d," +
            "because event date is before current time";
    public final static String USER_EVENT_NOT_FOUND_MESSAGE = "Event with id %d was not found";
    public final static String EVENT_ADMIN_UPDATE_STATE_MESSAGE = "Cannot publish the event with id %d, because it's not in the right state: %s";
    public final static String EVENT_USER_UPDATE_STATE_MESSAGE = "Cannot update the event with id %d, because it's not in the right state: %s";

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;
    private final EventMapper eventMapper;

    private final EntityManager entityManager;


    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {
        Optional<User> optionalInitiator = userRepository.findById(userId);
        if (optionalInitiator.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Category> optionalCategory = categoryRepository.findById(eventDto.getCategory());
        if (optionalCategory.isEmpty())
            throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, userId);
        if (LocalDateTime.parse(eventDto.getEventDate(), formatter).isBefore(LocalDateTime.now())) {
            throw new EventDateException(EVENT_DATE_BEFORE_CURRENT_MESSAGE, 0);
        }

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
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }
        Event event = eventMapper.mapToEvent(eventDto);
        event.setInitiator(optionalInitiator.get());
        event.setCategory(optionalCategory.get());
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0);
        return eventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    public EventFullDto getById(int id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, id);
        Event event = optionalEvent.get();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventWrongStateException(id, event.getState().name());
        }
        return eventMapper.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto findUserEventById(int userId, int eventId) {
        Optional<User> optionalInitiator = userRepository.findById(userId);
        if (optionalInitiator.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Event> optionalEvent = eventRepository.findByInitiator_IdAndId(userId, eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(USER_EVENT_NOT_FOUND_MESSAGE, eventId);
        return eventMapper.mapToEventFullDto(optionalEvent.get());
    }

    @Override
    public List<EventFullDto> findAllEvents(String text,
                                            List<Integer> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            int from,
                                            int size) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new EventWrongDateRangeException(rangeStart, rangeEnd);
            }
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        if (text != null) {
            Predicate likeInAnnotation = criteriaBuilder.like(
                    criteriaBuilder.lower(root.<String>get("annotation")),
                    "%" + text.toLowerCase() + "%"
            );
            Predicate likeInDescription = criteriaBuilder.like(
                    criteriaBuilder.lower(root.<String>get("description")),
                    "%" + text.toLowerCase() + "%"
            );
            predicates.add(criteriaBuilder.or(likeInAnnotation, likeInDescription));
        }


        if (categories != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("category").in(categories)));
        }

        if (paid != null) {
            if (paid) {
                predicates.add(criteriaBuilder.isTrue(root.get("paid")));
            } else {
                predicates.add(criteriaBuilder.isFalse(root.get("paid")));
            }
        }

        if (rangeStart != null && rangeEnd != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        } else {
            predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }

        if (onlyAvailable) {
            predicates.add(criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }

        Order order = criteriaBuilder.asc(root.get("id"));
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                order = criteriaBuilder.asc(root.get("eventDate"));
            }
            if (sort.equals("VIEWS")) {
                order = criteriaBuilder.asc(root.get("views"));
            }
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()])).orderBy(order);
        TypedQuery<Event> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);

        return query.getResultList()
                .stream()
                .map(eventMapper::mapToEventFullDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<EventFullDto> findAllEventsByUser(int userId, int from, int size) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        return eventRepository.getEvents(userId, from, size)
                .stream()
                .map(eventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> findAllEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        if (users != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("initiator").in(users)));
        }

        if (states != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("state").as(String.class).in(states)));
        }

        if (categories != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("category").in(categories)));
        }

        if (rangeStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Event> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);
        return query.getResultList().stream().map(eventMapper::mapToEventFullDto).collect(Collectors.toList());

    }

    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest userEventRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Event> optionalEvent = eventRepository.findByInitiator_IdAndId(userId, eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(USER_EVENT_NOT_FOUND_MESSAGE, eventId);
        Event oldEvent = optionalEvent.get();

        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new EventUpdateStateException(EVENT_USER_UPDATE_STATE_MESSAGE, eventId, oldEvent.getState().name());
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, oldEvent.getEventDate());
        if (duration.toHours() < 2)
            throw new EventDateException(EVENT_USER_UPDATE_DATE_MESSAGE, eventId);

        if (userEventRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(userEventRequest.getAnnotation());
        }
        if (userEventRequest.getCategory() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(userEventRequest.getCategory());
            if (optionalCategory.isEmpty())
                throw new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE, userEventRequest.getCategory());
            oldEvent.setCategory(optionalCategory.get());
        }
        if (userEventRequest.getDescription() != null) {
            oldEvent.setDescription(userEventRequest.getDescription());
        }
        if (userEventRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(userEventRequest.getEventDate(), formatter);
            if (eventDate.isBefore(currentDateTime))
                throw new EventDateException(EVENT_DATE_BEFORE_CURRENT_MESSAGE, eventId);
            oldEvent.setEventDate(LocalDateTime.parse(userEventRequest.getEventDate(), formatter));
        }
        if (userEventRequest.getLocation() != null) {
            LocationDto locationDto = userEventRequest.getLocation();
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

        if (userEventRequest.getPaid() != null) {
            oldEvent.setPaid(userEventRequest.getPaid());
        }

        if (userEventRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(userEventRequest.getParticipantLimit());
        }

        if (userEventRequest.getRequestModeration() != null) {
            oldEvent.setRequestModeration(userEventRequest.getRequestModeration());
        }

        if (userEventRequest.getStateAction() != null) {
            if (userEventRequest.getStateAction().equals(UserStateAction.SEND_TO_REVIEW.name())) {
                oldEvent.setState(EventState.PENDING);
            } else {
                oldEvent.setState(EventState.CANCELED);
            }
        }

        if (userEventRequest.getTitle() != null) {
            oldEvent.setTitle(userEventRequest.getTitle());
        }
        return eventMapper.mapToEventFullDto(eventRepository.save(oldEvent));

    }


    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest adminEventRequest) {

        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        Event oldEvent = optionalEvent.get();
        if (!oldEvent.getState().equals(EventState.PENDING)) {
            throw new EventUpdateStateException(EVENT_ADMIN_UPDATE_STATE_MESSAGE, eventId, oldEvent.getState().name());
        }

        LocalDateTime publishDate = LocalDateTime.now();
        Duration duration = Duration.between(publishDate, oldEvent.getEventDate());
        if (duration.toHours() < 1)
            throw new EventDateException(EVENT_ADMIN_UPDATE_DATE_MESSAGE, eventId);

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
            LocalDateTime eventDate = LocalDateTime.parse(adminEventRequest.getEventDate(), formatter);
            if (eventDate.isBefore(publishDate))
                throw new EventDateException(EVENT_DATE_BEFORE_CURRENT_MESSAGE, eventId);
            oldEvent.setEventDate(eventDate);
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
            if (adminEventRequest.getStateAction().equals(AdminStateAction.PUBLISH_EVENT.name())) {
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
