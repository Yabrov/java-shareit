package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, Integer> emails = new HashMap<>();
    private Integer currentId = 1;

    public User findUserById(Integer id) {
        return users.get(id);
    }

    public User deleteUser(User user) {
        users.remove(user.getId());
        emails.remove(user.getEmail());
        return user;
    }

    public User saveUser(User user) {
        if (emails.containsKey(user.getEmail())) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
        User savingUser = user.withId(currentId++);
        users.put(savingUser.getId(), savingUser);
        emails.put(savingUser.getEmail(), savingUser.getId());
        return savingUser;
    }

    public User updateUser(User user) {
        User oldUser = findUserById(user.getId());
        if (emails.containsKey(user.getEmail()) && !oldUser.getEmail().equals(user.getEmail())) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
        users.replace(user.getId(), user);
        emails.remove(oldUser.getEmail());
        emails.put(user.getEmail(), user.getId());
        return user;
    }

    public Collection<User> findAllUsers() {
        return users.values();
    }
}
