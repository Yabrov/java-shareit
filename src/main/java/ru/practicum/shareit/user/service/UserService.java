package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NullEmailException;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserRepository repository;
    private final Converter<User, UserDto> userMapper;

    public UserDto findUserById(Integer id) {
        return userMapper.convert(repository.findUserById(id));
    }

    public UserDto updateUser(User user) {
        return userMapper.convert(repository.updateUser(user));
    }

    public UserDto createUser(User user) {
        if (user.getEmail() == null) {
            throw new NullEmailException();
        }
        return userMapper.convert(repository.saveUser(user));
    }

    public UserDto deleteUser(Integer id) {
        return userMapper.convert(repository.deleteUser(id));
    }

    public Collection<UserDto> getAllUsers() {
        return repository
                .findAllUsers()
                .stream()
                .map(userMapper::convert)
                .collect(Collectors.toList());
    }
}
