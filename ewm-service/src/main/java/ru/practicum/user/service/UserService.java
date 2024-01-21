package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    List<UserDto> findAllByIds(List<Integer> ids, int from, int size);

    void remove(int userId);
}
