package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(
            null,
            "test_name",
            "test_email@test.domain.com"
    );

    private final Long expectedUserId = 1L;

    @Test
    @DisplayName("Create valid user test")
    void createValidUserTest() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto.withId(expectedUserId));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).createUser(any());
    }

    @Test
    @DisplayName("Create user with null email test")
    void createUserWithNullEmailTest() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto.withEmail(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Create user with wrong email test")
    void createUserWithWrongEmailTest() throws Exception {
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto.withEmail("abc")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Create user with duplicate email test")
    void createUserDuplicateEmailTest() throws Exception {
        String duplicateEmail = "test_email@test.domain.com";
        when(userService.createUser(any()))
                .thenThrow(new EmailUniqueViolationException(duplicateEmail));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto.withId(1L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    @DisplayName("Get existing user test")
    void getExistingUserTest() throws Exception {
        when(userService.findUserById(anyLong()))
                .thenReturn(userDto.withId(expectedUserId));
        mvc.perform(get("/users/" + expectedUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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
        mvc.perform(get("/users/" + expectedUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    @DisplayName("Get user with invalid id test")
    void getUserWithInvalidIdTest() throws Exception {
        mvc.perform(get("/users/someId")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, never()).findUserById(anyLong());
    }

    @Test
    @DisplayName("Update existing user test")
    void updateExistingUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDto.withId(expectedUserId).withName("Updated name"));
        mvc.perform(patch("/users/" + expectedUserId)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
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
        mvc.perform(patch("/users/" + expectedUserId)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing user test")
    void deleteExistingUserTest() throws Exception {
        when(userService.deleteUser(anyLong()))
                .thenReturn(userDto.withId(expectedUserId));
        mvc.perform(delete("/users/" + expectedUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
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
        mvc.perform(delete("/users/" + expectedUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(userService, times(1)).deleteUser(anyLong());
    }

    @Test
    @DisplayName("Get all users test")
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(Collections.emptyList());
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(userService, times(1)).getAllUsers();
    }
}
