package ru.practicum.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCommentDto {
    @NotBlank
    @Size(min = 3, max = 500)
    private String text;
}
