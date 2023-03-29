package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserDtoMapper;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.DatabaseUserRepositoryImpl;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                UserServiceImpl.class,
                UserDtoMapper.class,
                UserMapper.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@MockBean(classes = DatabaseUserRepositoryImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UserRepository userRepository;

    private final UserService userService;

    private final Long expectedUserId = 1L;

    private final UserDto userDto = new UserDto(
            expectedUserId,
            "test_name",
            "test_email@test.domain.com"
    );

    private final User user = new User(
            expectedUserId,
            userDto.getName(),
            userDto.getEmail()
    );

    @Test
    @DisplayName("Create valid user test")
    void createValidUserTest() throws Exception {
        when(userRepository.saveUser(any())).thenReturn(user);
        UserDto result = userService.createUser(userDto);
        assertThat(result.getId()).isEqualTo(expectedUserId);
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).saveUser(any());
    }

    @Test
    @DisplayName("Get existing user test")
    void getExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        UserDto result = userService.findUserById(expectedUserId);
        assertThat(result.getId()).isEqualTo(expectedUserId);
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Get not existing user test")
    void getNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.findUserById(expectedUserId));
        verify(userRepository, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Update existing user test")
    void updateExistingUserTest() throws Exception {
        String updatedName = "updated_test_name";
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(userRepository.updateUser(any())).thenReturn(user.withName(updatedName));
        UserDto result = userService.updateUser(expectedUserId, userDto);
        assertThat(result.getId()).isEqualTo(expectedUserId);
        assertThat(result.getName()).isEqualTo(updatedName);
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(userRepository, times(1)).updateUser(any());
    }

    @Test
    @DisplayName("Update not existing user test")
    void updateNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.updateUser(expectedUserId, userDto));
        verify(userRepository, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Delete user test")
    void deleteUserTest() throws Exception {
        when(userRepository.deleteUser(any())).thenReturn(user);
        UserDto result = userService.deleteUser(expectedUserId);
        assertThat(result.getId()).isEqualTo(expectedUserId);
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).deleteUser(any());
    }

    @Test
    @DisplayName("Get all users test")
    void getAllUsersTest() throws Exception {
        when(userRepository.findAllUsers()).thenReturn(Lists.list(user));
        List<UserDto> result = new ArrayList<>(userService.getAllUsers());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expectedUserId);
        assertThat(result.get(0).getName()).isEqualTo(userDto.getName());
        assertThat(result.get(0).getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).findAllUsers();
    }
}
