package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.CommentCreateException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final ItemDto itemDto = new ItemDto(
            null,
            "test_name",
            "test_description",
            Boolean.FALSE,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final CommentDto commentDto = new CommentDto(
            null,
            "test_comment_text",
            "test_author_name",
            LocalDateTime.now()
    );

    private final Long xSharerUserId = 999L;
    private final Long expectedItemId = 1L;

    @Test
    @DisplayName("Create valid item test")
    void createValidItemTest() throws Exception {
        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto.withId(expectedItemId));
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemService, times(1)).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with empty name test")
    void createItemWithEmptyNameByTest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withName("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null name test")
    void createItemWithNullNameByTest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withName(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with empty description test")
    void createItemWithEmptyDescriptionByTest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withDescription("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null description test")
    void createItemWithNullDescriptionByTest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withDescription(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create item with null available test")
    void createItemWithNullAvailableByTest() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withAvailable(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createItem(anyLong(), any());
    }

    @Test
    @DisplayName("Get existing item test")
    void getExistingItemTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto.withId(expectedItemId));
        mvc.perform(get("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get not existing item test")
    void getNotExistingItemTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        mvc.perform(get("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get item by not existing user test")
    void getItemByNotExistingUserTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenThrow(new UserNotFoundException(xSharerUserId));
        mvc.perform(get("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Update existing item test")
    void updateExistingItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto.withId(expectedItemId));
        mvc.perform(patch("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withId(expectedItemId)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Update item by other user test")
    void updateItemByOtherUserTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new WrongItemOwnerException(xSharerUserId, expectedItemId));
        mvc.perform(patch("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withId(expectedItemId)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Update item by not existing user test")
    void updateItemByNotExistingUserTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new UserNotFoundException(xSharerUserId));
        mvc.perform(patch("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withId(expectedItemId)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Update not existing item test")
    void updateNotExistingItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        mvc.perform(patch("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemDto.withId(expectedItemId)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing item test")
    void deleteExistingItemTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenReturn(itemDto.withId(expectedItemId));
        mvc.perform(delete("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments())));
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Delete item by other user test")
    void deleteItemByOtherUserTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenThrow(new WrongItemOwnerException(xSharerUserId, expectedItemId));
        mvc.perform(delete("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Delte item by not existing user test")
    void deleteItemByNotExistingUserTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenThrow(new UserNotFoundException(xSharerUserId));
        mvc.perform(delete("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Delete not existing item test")
    void deleteNotExistingItemTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        mvc.perform(delete("/items/" + expectedItemId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all items test")
    void getAllItemsTest() throws Exception {
        when(itemService.getAllItems(anyLong(), any(), any()))
                .thenReturn(Lists.list(itemDto.withId(expectedItemId)));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$[0].lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$[0].nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments())));
        verify(itemService, times(1)).getAllItems(anyLong(), any(), any());
    }

    @Test
    @DisplayName("Get all items with invalid page params test")
    void getAllItemsWithInvalidPageParamsTest() throws Exception {
        when(itemService.getAllItems(anyLong(), anyInt(), anyInt()))
                .thenThrow(new InvalidPaginationParamsException(-1, 0));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("from", "-1")
                        .queryParam("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getAllItems(anyLong(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Search items test")
    void searchItemsTest() throws Exception {
        when(itemService.searchItem(anyString(), any(), any()))
                .thenReturn(Lists.list(itemDto.withId(expectedItemId)));
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedItemId), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId())))
                .andExpect(jsonPath("$[0].lastBooking", is(itemDto.getLastBooking())))
                .andExpect(jsonPath("$[0].nextBooking", is(itemDto.getNextBooking())))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments())));
        verify(itemService, times(1)).searchItem(anyString(), any(), any());
    }

    @Test
    @DisplayName("Search items with empty text param test")
    void searchItemsWithEmptyTextParamTest() throws Exception {
        when(itemService.getAllItems(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("text", "")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
        verify(itemService, times(1)).searchItem(anyString(), any(), any());
    }

    @Test
    @DisplayName("Create comment test")
    void createCommentTest() throws Exception {
        Long expectedCommentId = 1L;
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto.withId(expectedCommentId));
        mvc.perform(post("/items/" + expectedItemId + "/comment")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedCommentId), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment with empty text test")
    void createCommentWithEmptyTextTest() throws Exception {
        mvc.perform(post("/items/" + expectedItemId + "/comment")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(commentDto.withText("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment with null text test")
    void createCommentWithNullTextTest() throws Exception {
        mvc.perform(post("/items/" + expectedItemId + "/comment")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(commentDto.withText(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, never()).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment by wrong user test")
    void createCommentByWrongUserTest() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenThrow(new CommentCreateException("some message"));
        mvc.perform(post("/items/" + expectedItemId + "/comment")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any());
    }
}
