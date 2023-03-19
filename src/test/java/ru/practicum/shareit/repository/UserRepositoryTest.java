package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.config.IdReducer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.DatabaseUserRepositoryImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Rollback
@DataJpaTest
@Transactional(readOnly = true)
@Import(value = {DatabaseUserRepositoryImpl.class, IdReducer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {

    private final UserRepository userRepository;

    private final IdReducer idReducer;

    @BeforeEach
    public void setUp() throws SQLException {
        idReducer.resetAutoIncrementColumns("users");
    }

    private final Long expectedUserId = 1L;

    private final User user = new User(
            "test_name",
            "email@test.com"
    ).withId(expectedUserId);

    @Test
    @DisplayName("Find user by id test")
    @Sql(statements = "INSERT INTO users(name, email) VALUES ('test_name', 'email@test.com')")
    public void findUserByIdTest() throws Exception {
        assertThat(userRepository.findUserById(expectedUserId)).isEqualTo(user);
    }

    @Transactional
    @Test
    @DisplayName("Insert valid user test")
    public void insertValidUserTest() throws Exception {
        assertThat(userRepository.saveUser(user)).isEqualTo(user);
        assertThat(userRepository.findUserById(expectedUserId)).isEqualTo(user);
    }
}
