package ru.practicum.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.model.Comment;

@Component
public class CommentMapper {
    CommentResponseDto mapToCommentDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }

}
