package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

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
    private UserClient userClient;

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
        when(userClient.createUser(any())).thenReturn(ResponseEntity.ok(userDto));
        performPostRequests("/users", userDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userClient, times(1)).createUser(any());
    }

    @Test
    @DisplayName("Create user with null email test")
    void createUserWithNullEmailTest() throws Exception {
        performPostRequests("/users", userDto.withEmail(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(userClient, never()).createUser(any());
    }

    @Test
    @DisplayName("Create user with wrong email test")
    void createUserWithWrongEmailTest() throws Exception {
        performPostRequests("/users", userDto.withEmail("abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(userClient, never()).createUser(any());
    }

    @Test
    @DisplayName("Get existing user test")
    void getExistingUserTest() throws Exception {
        when(userClient.getUser(anyLong())).thenReturn(ResponseEntity.ok(userDto));
        performGetRequests("/users/" + expectedUserId, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userClient, times(1)).getUser(anyLong());
    }

    @Test
    @DisplayName("Update existing user test")
    void updateExistingUserTest() throws Exception {
        when(userClient.updateUser(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(userDto.withName("Updated name")));
        performPatchRequests("/users/" + expectedUserId, userDto, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is("Updated name")))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userClient, times(1)).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing user test")
    void deleteExistingUserTest() throws Exception {
        when(userClient.deleteUser(anyLong())).thenReturn(ResponseEntity.ok(userDto));
        performDeleteRequests("/users/" + expectedUserId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUserId), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userClient, times(1)).deleteUser(anyLong());
    }

    @Test
    @DisplayName("Get all users test")
    void getAllUsersTest() throws Exception {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.ok(Lists.list(userDto)));
        performGetRequests("/users", new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
        verify(userClient, times(1)).getAllUsers();
    }
}
