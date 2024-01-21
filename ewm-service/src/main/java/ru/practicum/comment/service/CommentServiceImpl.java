package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentUserRequest;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.*;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.event.service.EventServiceImpl.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final CommentMapper mapper;
    private final EntityManager entityManager;

    public static final String COMMENT_NOT_FOUND_MESSAGE = "Comment with id %d was not found";

    @Override
    public CommentResponseDto addComment(int userId, int eventId, NewCommentDto newCommentDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        User user = optionalUser.get();
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        Event event = optionalEvent.get();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventNotPublishedException(eventId);
        }
        Comment comment = Comment.builder()
                .user(user).event(event).text(newCommentDto.getText()).created(LocalDateTime.now())
                .build();
        CommentResponseDto commentDto = mapper.mapToCommentDto(commentRepository.save(comment));
        commentDto.setUser(userMapper.mapToUserShortDto(comment.getUser()));
        commentDto.setEvent(eventMapper.mapToEventShortDto(comment.getEvent()));
        return commentDto;
    }

    @Override
    public CommentResponseDto findUserCommentById(int userId, int comId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Comment> optionalComment = commentRepository.findById(comId);
        if (optionalComment.isEmpty())
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE, comId);
        Comment comment = optionalComment.get();
        if (comment.getUser().getId() != userId)
            throw new CommentWrongOwnerException(comId, userId);
        CommentResponseDto commentDto = mapper.mapToCommentDto(commentRepository.save(comment));
        commentDto.setUser(userMapper.mapToUserShortDto(comment.getUser()));
        commentDto.setEvent(eventMapper.mapToEventShortDto(comment.getEvent()));
        return commentDto;

    }

    @Override
    public List<CommentResponseDto> findAllCommentsByAdmin(List<Integer> users, List<Integer> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new WrongDateRangeException(rangeStart, rangeEnd);
            }
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (users != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("user").in(users)));
        }
        if (events != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("event").in(events)));
        }

        if (rangeStart != null && rangeEnd != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), rangeStart));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), rangeEnd));
        } else {
            predicates.add(criteriaBuilder.greaterThan(root.get("created"), LocalDateTime.now().minusDays(1)));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Comment> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);

        List<Comment> comments = query.getResultList();

        return setUserAndEventToCommentDto(comments);
    }

    @Override
    public List<CommentResponseDto> findAllComments(String text, List<Integer> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (text != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("text")),
                    "%" + text.toLowerCase() + "%")
            );
        }
        if (events != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("event").in(events)));
        }

        if (rangeStart != null && rangeEnd != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), rangeStart));
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), rangeEnd));
        } else {
            predicates.add(criteriaBuilder.greaterThan(root.get("created"), LocalDateTime.now().minusDays(1)));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Comment> query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(from)
                .setMaxResults(size);

        List<Comment> comments = query.getResultList();
        return setUserAndEventToCommentDto(comments);
    }

    @Override
    public List<CommentResponseDto> findAllCommentsByUser(int userId, int from, int size) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);

        List<Comment> comments = commentRepository.findByUser_Id(userId);
        return setUserAndEventToCommentDto(comments);
    }

    @Override
    public CommentResponseDto updateCommentByUser(int userId, int comId, UpdateCommentUserRequest commentRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Comment> optionalComment = commentRepository.findById(comId);
        if (optionalComment.isEmpty())
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE, comId);
        Comment comment = optionalComment.get();
        if (comment.getUser().getId() != userId)
            throw new CommentWrongOwnerException(comId, userId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, comment.getCreated());
        if (duration.toMinutes() > 5)
            throw new CommentExceedsTimeLimitException(comId);
        comment.setText(commentRequest.getText());
        CommentResponseDto commentDto = mapper.mapToCommentDto(commentRepository.save(comment));
        commentDto.setUser(userMapper.mapToUserShortDto(comment.getUser()));
        commentDto.setEvent(eventMapper.mapToEventShortDto(comment.getEvent()));
        return commentDto;
    }

    @Override
    public void deleteCommentById(int comId) {
        Optional<Comment> optionalComment = commentRepository.findById(comId);
        if (optionalComment.isEmpty())
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE, comId);
        commentRepository.delete(optionalComment.get());
    }

    @Override
    public void deleteUserCommentById(int userId, int comId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Comment> optionalComment = commentRepository.findById(comId);
        if (optionalComment.isEmpty())
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE, comId);
        Comment comment = optionalComment.get();
        if (comment.getUser().getId() != userId)
            throw new CommentWrongOwnerException(comId, userId);
        commentRepository.delete(comment);
    }

    private List<CommentResponseDto> setUserAndEventToCommentDto(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto commentDto = mapper.mapToCommentDto(comment);
            commentDto.setUser(userMapper.mapToUserShortDto(comment.getUser()));
            commentDto.setEvent(eventMapper.mapToEventShortDto(comment.getEvent()));
            result.add(commentDto);
        }
        return result;
    }
}
