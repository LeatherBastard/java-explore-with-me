package ru.practicum.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.model.Comment;

import static ru.practicum.event.mapper.EventMapper.formatter;

@Component
public class CommentMapper {
    public CommentResponseDto mapToCommentDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated().format(formatter))
                .build();
    }

}
