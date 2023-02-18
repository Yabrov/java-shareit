package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NullEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final InMemoryUserRepository userRepository;
    private final Converter<User, UserDto> userMapper;

    public UserDto findUserById(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return userMapper.convert(user);
    }

    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        User newUser = User
                .builder()
                .id(userId)
                .name(userDto.getName() == null ? user.getName() : userDto.getName())
                .email(userDto.getEmail() == null ? user.getEmail() : userDto.getEmail())
                .build();
        User updatedUser = userRepository.updateUser(newUser);
        log.info("User with id {} updated.", userId);
        return userMapper.convert(updatedUser);
    }

    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new NullEmailException();
        }
        User user = User
                .builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        User savedUser = userRepository.saveUser(user);
        log.info("User with id {} saved.", savedUser.getId());
        return userMapper.convert(savedUser);
    }

    public UserDto deleteUser(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        User deletedUser = userRepository.deleteUser(user);
        log.info("User with id {} deleted.", userId);
        return userMapper.convert(deletedUser);
    }

    public Collection<UserDto> getAllUsers() {
        return userRepository
                .findAllUsers()
                .stream()
                .map(userMapper::convert)
                .collect(Collectors.toList());
    }
}
