package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(int userId, int eventId, NewCommentDto newCommentDto);

    CommentResponseDto findUserCommentById(int userId, int comId);

    List<CommentResponseDto> findAllCommentsByAdmin(List<Integer> users, List<Integer> events,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<CommentResponseDto> findAllComments(String text, List<Integer> events,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<CommentResponseDto> findAllCommentsByUser(int userId, int from, int size);

    CommentResponseDto updateCommentByUser(int userId, int commentId, UpdateCommentUserRequest commentRequest);

    void deleteCommentById(int commentId);

    void deleteUserCommentById(int userId, int comId);

}
