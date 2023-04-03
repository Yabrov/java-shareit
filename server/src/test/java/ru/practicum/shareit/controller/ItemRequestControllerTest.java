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
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest extends AbstractControllerTest {

    @Autowired
    public ItemRequestControllerTest(ObjectMapper mapper, MockMvc mvc) {
        this.mapper = mapper;
        this.mvc = mvc;
    }

    @MockBean
    private ItemRequestService requestService;

    @Override
    protected Long getXSharerUserId() {
        return 999L;
    }

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
        performPostRequests("/requests", itemRequestDto)
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
                .thenThrow(new UserNotFoundException(getXSharerUserId()));
        performPostRequests("/requests", itemRequestDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, times(1)).createItemRequest(anyLong(), any());
    }

    @Test
    @DisplayName("Get existing item request test")
    void getExistingItemRequestTest() throws Exception {
        when(requestService.getItemRequest(anyLong(), anyLong())).thenReturn(itemRequestDto);
        performGetRequests("/requests/" + expectedRequestId, new LinkedMultiValueMap<>())
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
        performGetRequests("/requests/" + expectedRequestId, new LinkedMultiValueMap<>())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(requestService, times(1)).getItemRequest(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get owner item requests test")
    void getOwnerItemRequestsTest() throws Exception {
        when(requestService.getOwnItemRequests(anyLong())).thenReturn(Lists.list(itemRequestDto));
        performGetRequests("/requests", new LinkedMultiValueMap<>())
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/requests/all", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedRequestId), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto.getItems())));
        verify(requestService, times(1)).getAllItemRequests(anyLong(), anyInt(), anyInt());
    }
}
