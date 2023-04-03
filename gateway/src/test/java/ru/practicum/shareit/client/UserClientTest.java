package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserClientTest {

    private final RestTemplate rest = Mockito.mock(RestTemplate.class);

    private final UserClient userClient = new UserClient(rest);

    private final UserDto userDto = new UserDto(
            1L,
            "test_name",
            "test_email@test.domain.com"
    );

    @BeforeEach
    void init() {
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(ResponseEntity.ok().body(null));
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any(),
                        anyMap()))
                .thenReturn(ResponseEntity.ok(null));
    }

    @Test
    void createUserTest() {
        assertThat(userClient.createUser(userDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void updateUserTest() {
        assertThat(userClient.updateUser(1L, userDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void deleteUserTest() {
        assertThat(userClient.deleteUser(1L)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void getAllUsersTest() {
        assertThat(userClient.getAllUsers()).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }
}
