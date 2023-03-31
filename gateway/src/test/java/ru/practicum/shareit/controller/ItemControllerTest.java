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
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest extends AbstractControllerTest {

    @Autowired
    public ItemControllerTest(ObjectMapper mapper, MockMvc mvc) {
        this.mapper = mapper;
        this.mvc = mvc;
    }

    @MockBean
    private ItemClient itemClient;

    @Override
    protected Long getXSharerUserId() {
        return 999L;
    }

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

    @Test
    @DisplayName("Create valid item test")
    void createValidItemTest() throws Exception {
        when(itemClient.createItem(anyLong(), any())).thenReturn(ResponseEntity.ok(itemDto));
        performPostRequests("/items", itemDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with empty name test")
    void createItemWithEmptyNameByTest() throws Exception {
        performPostRequests("/items", itemDto.withName(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null name test")
    void createItemWithNullNameByTest() throws Exception {
        performPostRequests("/items", itemDto.withName(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with empty description test")
    void createItemWithEmptyDescriptionByTest() throws Exception {
        performPostRequests("/items", itemDto.withDescription(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null description test")
    void createItemWithNullDescriptionByTest() throws Exception {
        performPostRequests("/items", itemDto.withDescription(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null available test")
    void createItemWithNullAvailableByTest() throws Exception {
        performPostRequests("/items", itemDto.withAvailable(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Get existing item test")
    void getExistingItemTest() throws Exception {
        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(itemDto));
        performGetRequests("/items/" + expectedItemId, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Update existing item test")
    void updateExistingItemTest() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any())).thenReturn(ResponseEntity.ok(itemDto));
        performPatchRequests("/items/" + expectedItemId, itemDto, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing item test")
    void deleteExistingItemTest() throws Exception {
        when(itemClient.deleteItem(anyLong(), anyLong())).thenReturn(ResponseEntity.ok(itemDto));
        performDeleteRequests("/items/" + expectedItemId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all items test")
    void getAllItemsTest() throws Exception {
        when(itemClient.getAllItems(anyLong(), any(), any()))
                .thenReturn(ResponseEntity.ok(Lists.list(itemDto)));
        performGetRequests("/items", new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).getAllItems(anyLong(), any(), any());
    }

    @Test
    @DisplayName("Search items test")
    void searchItemsTest() throws Exception {
        when(itemClient.searchItems(anyLong(), anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(Lists.list(itemDto)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("text", Lists.list("text"));
        performGetRequests("/items/search", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments())));
        verify(itemClient, times(1)).searchItems(anyLong(), anyString(), any(), any());
    }

    @Test
    @DisplayName("Search items with empty text param test")
    void searchItemsWithEmptyTextParamTest() throws Exception {
        when(itemClient.searchItems(anyLong(), anyString(), any(), any()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("text", Lists.list(""));
        performGetRequests("/items/search", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
        verify(itemClient, times(1)).searchItems(anyLong(), anyString(), any(), any());
    }

    @Test
    @DisplayName("Create comment test")
    void createCommentTest() throws Exception {
        Long expectedCommentId = 1L;
        when(itemClient.createComment(anyLong(), anyLong(), any()))
                .thenReturn(ResponseEntity.ok(commentDto));
        performPostRequests("/items/" + expectedItemId + "/comment", commentDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedCommentId), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
        verify(itemClient, times(1)).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment with empty text test")
    void createCommentWithEmptyTextTest() throws Exception {
        performPostRequests("/items/" + expectedItemId + "/comment", commentDto.withText(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment with null text test")
    void createCommentWithNullTextTest() throws Exception {
        performPostRequests("/items/" + expectedItemId + "/comment", commentDto.withText(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemClient, never()).createComment(anyLong(), anyLong(), any());
    }
}
