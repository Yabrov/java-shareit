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
@Sql(scripts = "classpath:user_init.sql")
@Import(value = {DatabaseUserRepositoryImpl.class, IdReducer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    private final UserRepository userRepository;

    private final IdReducer idReducer;

    private final Long expectedUserId = 1L;

    private final User user = new User(
            "test_name",
            "email@test.com"
    ).withId(expectedUserId);

    @BeforeEach
    public void setUp() throws SQLException {
        idReducer.resetAutoIncrementColumns("users");
    }

    @Test
    @DisplayName("Find user by id test")
    void findUserByIdTest() throws Exception {
        assertThat(userRepository.findUserById(expectedUserId)).isEqualTo(user);
    }

    @Test
    @DisplayName("Find all users test")
    void findAllUsersTest() throws Exception {
        assertThat(userRepository.findAllUsers()).asList().isNotEmpty().contains(user);
    }
}
