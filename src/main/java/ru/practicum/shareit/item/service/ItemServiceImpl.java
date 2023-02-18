package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemCreateException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final InMemoryUserRepository userRepository;
    private final InMemoryItemRepository itemRepository;
    private final Converter<Item, ItemDto> itemMapper;

    @Override
    public ItemDto getItem(Integer itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        return itemMapper.convert(item);
    }

    @Override
    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        User owner = userRepository.findUserById(userId);
        if (owner == null) {
            throw new UserNotFoundException(userId);
        }
        if (itemDto.getAvailable() == null) {
            throw new ItemCreateException("Item availability is required.");
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ItemCreateException("Item create with empty name.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ItemCreateException("Item create with empty description.");
        }
        Item item = Item
                .builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();
        Item savedItem = itemRepository.saveItem(item);
        log.info("Item with id {} saved.", savedItem.getId());
        return itemMapper.convert(savedItem);
    }

    @Override
    public ItemDto updateItem(Integer userId, Integer itemId, ItemDto itemDto) {
        User owner = userRepository.findUserById(userId);
        if (owner == null) {
            throw new UserNotFoundException(userId);
        }
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemDto.getId());
        }
        if (!owner.getId().equals(item.getOwner().getId())) {
            throw new WrongItemOwnerException(owner.getId(), item.getId());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        Item updatedItem = itemRepository.updateItem(item);
        log.info("Item with id {} updated.", item.getId());
        return itemMapper.convert(updatedItem);
    }

    @Override
    public ItemDto deleteItem(Integer userId, Integer itemId) {
        User owner = userRepository.findUserById(userId);
        if (owner == null) {
            throw new UserNotFoundException(userId);
        }
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        if (!owner.getId().equals(item.getOwner().getId())) {
            throw new WrongItemOwnerException(owner.getId(), item.getId());
        }
        Item deletedItem = itemRepository.deleteItem(itemId);
        log.info("Item with id {} deleted.", itemId);
        return itemMapper.convert(deletedItem);
    }

    @Override
    public Collection<ItemDto> getAllItems(Integer userId) {
        return itemRepository
                .findAllItems(userId)
                .stream()
                .map(itemMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        return text.isEmpty()
                ? Collections.emptyList()
                : itemRepository
                .searchItem(text.toLowerCase())
                .stream()
                .map(itemMapper::convert)
                .collect(Collectors.toList());
    }
}
