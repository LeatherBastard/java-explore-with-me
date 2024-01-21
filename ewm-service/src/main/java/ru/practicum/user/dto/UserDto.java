package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
