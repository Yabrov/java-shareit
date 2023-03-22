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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mvc;

    private final Long xSharerUserId = 999L;

    private final Long expectedRequestId = 33L;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            expectedRequestId,
            "test_description",
            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            Collections.emptyList()
    );

    @Test
    @DisplayName("Create valid item request test")
    void createValidItemRequestTest() throws Exception {
        when(requestService.createItemRequest(anyLong(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedRequestId), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
        verify(requestService, times(1)).createItemRequest(anyLong(), any());
    }

    @Test
    @DisplayName("Create item request by not existing user test")
    void createItemRequestByNotExistingUserTest() throws Exception {
        when(requestService.createItemRequest(anyLong(), any()))
                .thenThrow(new UserNotFoundException(xSharerUserId));
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, times(1)).createItemRequest(anyLong(), any());
    }

    @Test
    @DisplayName("Create item request with empty description test")
    void createItemRequestWithEmptyDescriptionTest() throws Exception {
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemRequestDto.withDescription("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, never()).createItemRequest(anyLong(), any());
    }

    @Test
    @DisplayName("Create item request with null description test")
    void createItemRequestWithNullDescriptionTest() throws Exception {
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(itemRequestDto.withDescription(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, never()).createItemRequest(anyLong(), any());
    }

    @Test
    @DisplayName("Get existing item request test")
    void getExistingItemRequestTest() throws Exception {
        when(requestService.getItemRequest(anyLong(), anyLong())).thenReturn(itemRequestDto);
        mvc.perform(get("/requests/" + expectedRequestId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedRequestId), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
        verify(requestService, times(1)).getItemRequest(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get not existing item request test")
    void getNotExistingItemRequestTest() throws Exception {
        when(requestService.getItemRequest(anyLong(), anyLong()))
                .thenThrow(new ItemRequestNotFoundException(expectedRequestId));
        mvc.perform(get("/requests/" + expectedRequestId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, times(1)).getItemRequest(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get owner item requests test")
    void getOwnerItemRequestsTest() throws Exception {
        when(requestService.getOwnItemRequests(anyLong())).thenReturn(Lists.list(itemRequestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedRequestId), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto.getItems())));
        verify(requestService, times(1)).getOwnItemRequests(anyLong());
    }

    @Test
    @DisplayName("Get all item requests test")
    void getAllItemRequestsTest() throws Exception {
        when(requestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(Lists.list(itemRequestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("from", "0")
                        .queryParam("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedRequestId), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto.getItems())));
        verify(requestService, times(1)).getAllItemRequests(anyLong(), anyInt(), anyInt());
    }
}
