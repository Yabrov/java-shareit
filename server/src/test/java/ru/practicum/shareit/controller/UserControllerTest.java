package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends AbstractControllerTest {

    @Autowired
    public UserControllerTest(ObjectMapper mapper, MockMvc mvc) {
        this.mapper = mapper;
        this.mvc = mvc;
    }

    @MockBean
    private UserService userService;

    @Override
    protected Long getXSharerUserId() {
        return 1L;
    }

    private final Long expectedUserId = 1L;

    private final UserDto userDto = new UserDto(
            expectedUserId,
            "test_name",
            "test_email@test.domain.com"
    );

    @Test
    @DisplayName("Create valid user test")
    void createValidUserTest() throws Exception {
        when(userService.createUser(any())).thenReturn(userDto);
        performPostRequests("/users", userDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).createUser(any());
    }

    @Test
    @DisplayName("Create user with duplicate email test")
    void createUserDuplicateEmailTest() throws Exception {
        String duplicateEmail = "test_email@test.domain.com";
        when(userService.createUser(any()))
                .thenThrow(new EmailUniqueViolationException(duplicateEmail));
        performPostRequests("/users", userDto)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    @DisplayName("Get existing user test")
    void getExistingUserTest() throws Exception {
        when(userService.findUserById(anyLong()))
                .thenReturn(userDto.withId(expectedUserId));
        performGetRequests("/users/" + expectedUserId, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Get not existing user test")
    void getNotExistingUserTest() throws Exception {
        when(userService.findUserById(anyLong()))
                .thenThrow(new UserNotFoundException(expectedUserId));
        performGetRequests("/users/" + expectedUserId, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Get user with invalid id test")
    void getUserWithInvalidIdTest() throws Exception {
        performGetRequests("/users/someId", new LinkedMultiValueMap<>())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, never()).findUserById(anyLong());
    }

    @Test
    @DisplayName("Update existing user test")
    void updateExistingUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDto.withName("Updated name"));
        performPatchRequests("/users/" + expectedUserId, userDto, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is("Updated name")))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Update not existing user test")
    void updateNotExistingUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenThrow(new UserNotFoundException(expectedUserId));
        performPatchRequests("/users/" + expectedUserId, userDto, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing user test")
    void deleteExistingUserTest() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(userDto);
        performDeleteRequests("/users/" + expectedUserId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).deleteUser(anyLong());
    }

    @Test
    @DisplayName("Delete not existing user test")
    void deleteNotExistingUserTest() throws Exception {
        when(userService.deleteUser(anyLong()))
                .thenThrow(new UserNotFoundException(expectedUserId));
        performDeleteRequests("/users/" + expectedUserId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).deleteUser(anyLong());
    }

    @Test
    @DisplayName("Get all users test")
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(Collections.emptyList());
        performGetRequests("/users", new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(userService, times(1)).getAllUsers();
    }
}
