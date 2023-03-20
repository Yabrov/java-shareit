package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserDtoMapper;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = {UserMapper.class, UserDtoMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoTest {

    private final Converter<User, UserDto> userMapper;

    private final Converter<UserDto, User> userDtoMapper;

    private final User user = new User(
            "test_name",
            "test_email"
    ).withId(1L);

    private final UserDto userDto = new UserDto(
            1L,
            "test_name",
            "test_email"
    );

    @Test
    @DisplayName("User entity mapper test")
    void userEntityMapperTest() throws Exception {
        assertThat(userMapper.convert(user)).isEqualTo(userDto);
    }

    @Test
    @DisplayName("User dto entity mapper test")
    void userDtoMapperTest() throws Exception {
        assertThat(userDtoMapper.convert(userDto).withId(1L)).isEqualTo(user);
    }
}
