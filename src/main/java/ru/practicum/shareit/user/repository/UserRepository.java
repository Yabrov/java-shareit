package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {

    User findUserById(Long id);

    User deleteUser(Long userId);

    User saveUser(User user);

    User updateUser(User user);

    Collection<User> findAllUsers();
}
