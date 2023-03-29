package ru.practicum.shareit.user.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;

import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class DatabaseUserRepositoryImpl implements UserRepository {

    private final JpaUserRepository userRepository;

    public DatabaseUserRepositoryImpl(@Lazy JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public User deleteUser(Long userId) {
        User deletedUser = userRepository.findById(userId).orElse(null);
        if (deletedUser != null) {
            userRepository.deleteById(userId);
        }
        return deletedUser;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailUniqueViolationException(user.getEmail());
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        return userRepository.findAll();
    }
}
