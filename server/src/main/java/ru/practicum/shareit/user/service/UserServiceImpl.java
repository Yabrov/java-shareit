package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Converter<User, UserDto> userMapper;
    private final Converter<UserDto, User> userDtoMapper;

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return userMapper.convert(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        User newUser = userDtoMapper.convert(userDto).withId(userId);
        newUser.setName(userDto.getName() == null ? user.getName() : userDto.getName());
        newUser.setEmail(userDto.getEmail() == null ? user.getEmail() : userDto.getEmail());
        User updatedUser = userRepository.updateUser(newUser);
        log.info("User with id {} updated.", userId);
        return userMapper.convert(updatedUser);
    }

    @Transactional
    @Override
    public UserDto createUser(@NotNull UserDto userDto) {
        User savedUser = userRepository.saveUser(userDtoMapper.convert(userDto));
        log.info("User with id {} saved.", savedUser.getId());
        return userMapper.convert(savedUser);
    }

    @Transactional
    @Override
    public UserDto deleteUser(Long userId) {
        User deletedUser = userRepository.deleteUser(userId);
        log.info("User with id {} deleted.", userId);
        return userMapper.convert(deletedUser);
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository
                .findAllUsers()
                .stream()
                .map(userMapper::convert)
                .collect(Collectors.toList());
    }
}
