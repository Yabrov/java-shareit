package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingLinkedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Converter<Item, ItemDto> itemMapper;
    private final Converter<ItemDto, Item> itemDtoMapper;
    private final Converter<Booking, BookingLinkedDto> bookingMapper;

    @Autowired
    public ItemServiceImpl(
            @Qualifier("databaseItemRepositoryImpl") ItemRepository itemRepository,
            @Qualifier("databaseUserRepositoryImpl") UserRepository userRepository,
            Converter<Item, ItemDto> itemMapper,
            Converter<Booking, BookingLinkedDto> bookingMapper,
            Converter<ItemDto, Item> itemDtoMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.itemDtoMapper = itemDtoMapper;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        Item item = itemRepository.findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }
        if (userId != null) {
            User user = userRepository.findUserById(userId);
            if (user == null) {
                throw new UserNotFoundException(userId);
            }
            if (item.getOwner().equals(user)) {
                return addNextAndLastBooking(itemMapper.convert(item));
            }
        }
        return itemMapper.convert(item);
    }

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.findUserById(userId);
        if (owner == null) {
            throw new UserNotFoundException(userId);
        }
        Item item = itemDtoMapper.convert(itemDto);
        item.setOwner(owner);
        Item savedItem = itemRepository.saveItem(item);
        log.info("Item with id {} saved.", savedItem.getId());
        return addNextAndLastBooking(itemMapper.convert(savedItem));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
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
        return addNextAndLastBooking(itemMapper.convert(updatedItem));
    }

    @Transactional
    @Override
    public ItemDto deleteItem(Long userId, Long itemId) {
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
        return addNextAndLastBooking(itemMapper.convert(deletedItem));
    }

    @Override
    public Collection<ItemDto> getAllItems(Long userId) {
        return itemRepository
                .findAllItems(userId)
                .stream()
                .filter(Item::getAvailable)
                .map(itemMapper::convert)
                .map(this::addNextAndLastBooking)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        return text.isEmpty()
                ? Collections.emptyList()
                : itemRepository
                .searchItem(text)
                .stream()
                .filter(Item::getAvailable)
                .map(itemMapper::convert)
                .map(this::addNextAndLastBooking)
                .collect(Collectors.toList());
    }

    private ItemDto addNextAndLastBooking(ItemDto itemDto) {
        if (itemDto == null) return null;
        Booking nextBooking = itemRepository.getNextBookingByItemId(itemDto.getId());
        if (nextBooking != null) {
            itemDto.setNextBooking(bookingMapper.convert(nextBooking));
        }
        Booking lastBooking = itemRepository.getLastBookingByItemId(itemDto.getId());
        if (lastBooking != null) {
            itemDto.setLastBooking(bookingMapper.convert(lastBooking));
        }
        return itemDto;
    }
}
