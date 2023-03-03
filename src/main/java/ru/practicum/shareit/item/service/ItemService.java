package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto getItem(Integer itemId);

    ItemDto createItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto);

    ItemDto deleteItem(Integer userId, Integer itemId);

    Collection<ItemDto> getAllItems(Integer userId);

    Collection<ItemDto> searchItem(String text);
}
