package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto getItemRequest(Long requestId);

    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> getOwnItemRequests(Long userId);

    Collection<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);
}
