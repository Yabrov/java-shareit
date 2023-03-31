package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemClientTest {

    private final RestTemplate rest = Mockito.mock(RestTemplate.class);

    private final ItemClient itemClient = new ItemClient(rest);

    private final Long expectedItemId = 1L;

    private final ItemDto itemDto = new ItemDto(
            expectedItemId,
            "test_name",
            "test_description",
            Boolean.FALSE,
            null,
            null
    );

    private final CommentDto commentDto = new CommentDto(
            1L,
            "test_comment_text",
            "test_author_name",
            LocalDateTime.now()
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
    void createItemTest() {
        assertThat(itemClient.createItem(1L, itemDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void updateItemTest() {
        assertThat(itemClient.updateItem(1L, 2L, itemDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void deleteItemTest() {
        assertThat(itemClient.deleteItem(1L, 2L)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void getItemTest() {
        assertThat(itemClient.getItem(1L, 2L)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void searchItemsTest() {
        assertThat(itemClient.searchItems(1L, "text", 0, 1)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void getAllItemsTest() {
        assertThat(itemClient.getAllItems(1L, 0, 1)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void createCommentTest() {
        assertThat(itemClient.createComment(1L, 2L, commentDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }
}
