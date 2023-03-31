package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemRequestClientTest {

    private final RestTemplate rest = Mockito.mock(RestTemplate.class);

    private final ItemRequestClient itemRequestClient = new ItemRequestClient(rest);

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "test_description",
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            Collections.emptyList()
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
    void createItemRequestTest() {
        assertThat(itemRequestClient.createItemRequest(1L, itemRequestDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void getItemRequestTest() {
        assertThat(itemRequestClient.getItemRequest(1L, 2L)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void getAllItemRequestsTest() {
        assertThat(itemRequestClient.getAllItemRequests(1L, 0, 1)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void getOwnItemRequestsTest() {
        assertThat(itemRequestClient.getOwnItemRequests(1L)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }
}
