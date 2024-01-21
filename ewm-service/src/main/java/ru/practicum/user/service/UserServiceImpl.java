package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.exception.UserEmailOccupiedException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (repository.findByEmail(userDto.getEmail()).isPresent())
            throw new UserEmailOccupiedException(userDto.getEmail());
        return mapper.mapToUserDto(repository.save(mapper.mapToUser(userDto)));
    }

    @Override
    public List<UserDto> findAllByIds(List<Integer> ids, int from, int size) {
        List<User> result;
        if (ids == null) {
            result = repository.findAllWithoutIds(from, size);
        } else {
            result = repository.findAllWithIds(ids, from, size);
        }
        return result.stream().map(mapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public void remove(int userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId));
        repository.delete(user);
    }

}
