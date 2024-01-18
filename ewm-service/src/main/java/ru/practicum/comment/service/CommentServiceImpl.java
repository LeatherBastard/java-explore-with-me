package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.participationrequest.repository.ParticipationRequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final CommentRepository commentRepository;

    /*
    Добавление комментария
        1.Если пользователь участник события
        2.Его заявка подтверждена
        3.Если событие уже прошло

     */
    @Override
    public CommentResponseDto addComment(int userId, int eventId, NewCommentDto newCommentDto) {

    }

    @Override
    public List<CommentResponseDto> findAllCommentsByAdmin(List<Integer> users, List<Integer> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public List<CommentResponseDto> findAllComments(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public List<CommentResponseDto> findAllCommentsByUser(int userId, int from, int size) {
        return null;
    }

    /*
    Редактирование комментария
        1.Если пользователь участник события
        2.Его заявка подтверждена
        3.Если событие уже прошло
        4.Если с момента написания комментария прошло не больше n-часов( добавить поле created в базу, потому что есть поиск по дате)
            в коде с событиями, есть вычисление часов
     */

    /*
    Удаления комментария от админа(айди комментария)
    1.Существет ли комментарий
    Удаление комментария от пользователя(айди комментария)
    1.Существует ли пользователь
    2.Существет ли комментарий
    3. Авторство пользователя

     */
}
