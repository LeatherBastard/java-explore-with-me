package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(int userId, NewCommentDto newCommentDto);

    List<CommentResponseDto> findAllCommentsByAdmin(List<Integer> users, List<Integer> events, List<String> states,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<CommentResponseDto> findAllComments(String text,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<CommentResponseDto> findAllCommentsByUser(int userId, int from, int size);

}
