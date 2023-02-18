package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto findUserById(Integer userId);

    UserDto updateUser(Integer userId, UserDto userDto);

    UserDto createUser(UserDto userDto);

    UserDto deleteUser(Integer userId);

    Collection<UserDto> getAllUsers();
}
