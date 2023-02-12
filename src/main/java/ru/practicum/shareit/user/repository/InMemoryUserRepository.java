package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, Integer> emails = new HashMap<>();
    private Integer currentId = 1;

    public User findUserById(Integer id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            log.error("User with id {} does not exist.", id);
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    public User deleteUser(Integer id) throws UserNotFoundException {
        User deletedUser = findUserById(id);
        users.remove(deletedUser.getId());
        emails.remove(deletedUser.getEmail());
        log.info("User with id {} deleted.", deletedUser.getId());
        return deletedUser;
    }

    public User saveUser(User user) throws EmailUniqueViolationException {
        if (emails.containsKey(user.getEmail())) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
        User savingUser = user.withId(currentId++);
        users.put(savingUser.getId(), savingUser);
        emails.put(savingUser.getEmail(), savingUser.getId());
        log.info("User with id {} saved.", savingUser.getId());
        return savingUser;
    }

    public User updateUser(User user) throws EmailUniqueViolationException, UserNotFoundException {
        User oldUser = findUserById(user.getId());
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
        if (emails.containsKey(user.getEmail()) && !oldUser.getEmail().equals(user.getEmail())) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
        users.replace(user.getId(), user);
        emails.remove(oldUser.getEmail());
        emails.put(user.getEmail(), user.getId());
        log.info("User with id {} updated.", user.getId());
        return user;
    }

    public Collection<User> findAllUsers() {
        return users.values();
    }
}
