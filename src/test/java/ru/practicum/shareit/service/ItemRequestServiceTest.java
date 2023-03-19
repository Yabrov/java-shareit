package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.config.PageBuilder;
import ru.practicum.shareit.item.dto.mapper.ItemToSimpleDtoMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryImpl;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.DatabaseUserRepositoryImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                ItemRequestServiceImpl.class,
                ItemRequestMapper.class,
                ItemRequestDtoMapper.class,
                ItemToSimpleDtoMapper.class,
                PageBuilder.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@MockBean(classes = {
        ItemRequestRepositoryImpl.class,
        DatabaseUserRepositoryImpl.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    private final ItemRequestService itemRequestService;

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final Long expectedItemRequestId = 1L;

    private final Long expectedUserId = 2L;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            expectedItemRequestId,
            "test_description",
            LocalDateTime.of(2043, 1, 1, 11, 0, 0),
            Collections.emptyList()
    );

    private final User user = new User(
            "test_user_name",
            "test_email@test.domain.com"
    ).withId(expectedUserId);

    private final ItemRequest itemRequest = new ItemRequest(
            itemRequestDto.getDescription(),
            user,
            null
    ).withId(expectedItemRequestId);

    @Test
    @DisplayName("Create valid item request test")
    void createValidItemRequestTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.createItemRequest(any()))
                .thenReturn(itemRequest.withRequestor(user).withCreated(created));
        ItemRequestDto result = itemRequestService.createItemRequest(expectedUserId, itemRequestDto);
        assertThat(result).isEqualTo(itemRequestDto.withCreated(created));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).createItemRequest(any());
    }

    @Test
    @DisplayName("Create item request with not existing user test")
    void createItemRequestWithBotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemRequestService.createItemRequest(expectedUserId, itemRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, never()).createItemRequest(any());
    }

    @Test
    @DisplayName("Get item request by existing user test")
    void getItemRequestByExistingUserTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getItemRequest(anyLong()))
                .thenReturn(itemRequest.withRequestor(user).withCreated(created));
        ItemRequestDto result = itemRequestService.getItemRequest(expectedUserId, expectedItemRequestId);
        assertThat(result).isEqualTo(itemRequestDto.withCreated(created));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).getItemRequest(anyLong());
    }

    @Test
    @DisplayName("Get item request by not existing user test")
    void getItemRequestByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemRequestService.getItemRequest(expectedUserId, expectedItemRequestId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, never()).getItemRequest(anyLong());
    }

    @Test
    @DisplayName("Get not existing item request test")
    void getNotExistingItemRequestTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getItemRequest(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemRequestNotFoundException.class)
                .isThrownBy(() -> itemRequestService.getItemRequest(expectedUserId, expectedItemRequestId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).getItemRequest(anyLong());
    }

    @Test
    @DisplayName("Get own item requests by existing user test")
    void getOwnItemRequestsByExistingUserTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getItemRequestsByOwner(any(), any()))
                .thenReturn(Lists.list(itemRequest.withRequestor(user).withCreated(created)));
        Collection<ItemRequestDto> result = itemRequestService.getOwnItemRequests(expectedUserId);
        assertThat(result).asList().isNotEmpty().contains(itemRequestDto.withCreated(created));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).getItemRequestsByOwner(any(), any());
    }

    @Test
    @DisplayName("Get own item requests by not existing user test")
    void getOwnItemRequestsByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemRequestService.getOwnItemRequests(expectedUserId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, never()).getItemRequestsByOwner(any(), any());
    }

    @Test
    @DisplayName("Get all item requests by existing user without page test")
    void getAllItemRequestsByExistingUserWithoutPageTest() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getAllItemRequests(any()))
                .thenReturn(Lists.list(itemRequest.withRequestor(user).withCreated(created)));
        Collection<ItemRequestDto> result = itemRequestService.getAllItemRequests(expectedUserId, null, null);
        assertThat(result).asList().isNotEmpty().contains(itemRequestDto.withCreated(created));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).getAllItemRequests(any());
    }

    @Test
    @DisplayName("Get all item requests by not existing user without page test")
    void getAllItemRequestsByNotExistingUserWithoutPageTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemRequestService.getAllItemRequests(expectedUserId, null, null));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, never()).getAllItemRequests(any());
    }

    @Test
    @DisplayName("Get all item requests by existing user with page test")
    void getAllItemRequestsByExistingUserWithPageTest() throws Exception {
        Integer from = 0;
        Integer size = 1;
        LocalDateTime created = LocalDateTime.now();
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getAllItemRequests(any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(itemRequest.withRequestor(user).withCreated(created))));
        Collection<ItemRequestDto> result = itemRequestService.getAllItemRequests(expectedUserId, from, size);
        assertThat(result).asList().isNotEmpty().contains(itemRequestDto.withCreated(created));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, times(1)).getAllItemRequests(any(), any());
    }

    @Test
    @DisplayName("Get all item requests by existing user with wrong page test")
    void getAllItemRequestsByExistingUserWithWrongPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> itemRequestService.getAllItemRequests(expectedUserId, from, size));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRequestRepository, never()).getAllItemRequests(any(), any());
    }
}
