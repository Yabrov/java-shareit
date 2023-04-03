package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.CommentCreateException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

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
    private ItemService itemService;

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

    @Test
    @DisplayName("Create valid item test")
    void createValidItemTest() throws Exception {
        when(itemService.createItem(anyLong(), any())).thenReturn(itemDto);
        performPostRequests("/items", itemDto)
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
    @DisplayName("Get existing item test")
    void getExistingItemTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemDto);
        performGetRequests("/items/" + expectedItemId, new LinkedMultiValueMap<>())
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
        performGetRequests("/items/" + expectedItemId, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get item by not existing user test")
    void getItemByNotExistingUserTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenThrow(new UserNotFoundException(getXSharerUserId()));
        performGetRequests("/items/" + expectedItemId, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Update existing item test")
    void updateExistingItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(itemDto);
        performPatchRequests("/items/" + expectedItemId, itemDto, new LinkedMultiValueMap<>())
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
                .thenThrow(new WrongItemOwnerException(getXSharerUserId(), expectedItemId));
        performPatchRequests("/items/" + expectedItemId, itemDto, new LinkedMultiValueMap<>())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Update item by not existing user test")
    void updateItemByNotExistingUserTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new UserNotFoundException(getXSharerUserId()));
        performPatchRequests("/items/" + expectedItemId, itemDto, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Update not existing item test")
    void updateNotExistingItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        performPatchRequests("/items/" + expectedItemId, itemDto, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Delete existing item test")
    void deleteExistingItemTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong())).thenReturn(itemDto);
        performDeleteRequests("/items/" + expectedItemId)
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
                .thenThrow(new WrongItemOwnerException(getXSharerUserId(), expectedItemId));
        performDeleteRequests("/items/" + expectedItemId)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Delete item by not existing user test")
    void deleteItemByNotExistingUserTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenThrow(new UserNotFoundException(getXSharerUserId()));
        performDeleteRequests("/items/" + expectedItemId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Delete not existing item test")
    void deleteNotExistingItemTest() throws Exception {
        when(itemService.deleteItem(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        performDeleteRequests("/items/" + expectedItemId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).deleteItem(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all items test")
    void getAllItemsTest() throws Exception {
        when(itemService.getAllItems(anyLong(), any(), any()))
                .thenReturn(Lists.list(itemDto));
        performGetRequests("/items", new LinkedMultiValueMap<>())
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("-1"));
        params.put("size", Lists.list("0"));
        performGetRequests("/items", params)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).getAllItems(anyLong(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Search items test")
    void searchItemsTest() throws Exception {
        when(itemService.searchItem(anyString(), any(), any()))
                .thenReturn(Lists.list(itemDto));
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("text", Lists.list(""));
        performGetRequests("/items/search", params)
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
        performPostRequests("/items/" + expectedItemId + "/comment", commentDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedCommentId), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @DisplayName("Create comment by wrong user test")
    void createCommentByWrongUserTest() throws Exception {
        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenThrow(new CommentCreateException("some message"));
        performPostRequests("/items/" + expectedItemId + "/comment", commentDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any());
    }
}
