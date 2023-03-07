package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto findUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto createUser(UserDto userDto);

    UserDto deleteUser(Long userId);

    Collection<UserDto> getAllUsers();
}
