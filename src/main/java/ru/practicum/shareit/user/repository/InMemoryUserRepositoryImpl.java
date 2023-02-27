package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emails = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public User findUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User deleteUser(Long userId) {
        User deletedUser = users.get(userId);
        emails.remove(deletedUser.getEmail());
        return users.remove(userId);
    }

    @Override
    public User saveUser(User user) {
        if (emails.containsKey(user.getEmail())) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
        user.setId(currentId++);
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
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

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }
}
