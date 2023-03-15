package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.config.PageBuilder;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final Converter<ItemRequest, ItemRequestDto> itemRequestMapper;
    private final Converter<ItemRequestDto, ItemRequest> itemRequestDtoMapper;
    private final PageBuilder pageBuilder;
    private final Sort requestsSort = Sort.by(Sort.Direction.DESC, "created");

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        ItemRequest itemRequest = itemRequestRepository.getItemRequest(requestId);
        if (itemRequest == null) {
            throw new ItemRequestNotFoundException(requestId);
        }
        return itemRequestMapper.convert(itemRequest);
    }

    @Transactional
    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        ItemRequest itemRequest = itemRequestDtoMapper.convert(itemRequestDto);
        itemRequest.setRequestor(user);
        return itemRequestMapper.convert(itemRequestRepository.createItemRequest(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getOwnItemRequests(Long userId) {
        User requestor = userRepository.findUserById(userId);
        if (requestor == null) {
            throw new UserNotFoundException(userId);
        }
        return itemRequestRepository
                .getItemRequestsByOwner(requestor, requestsSort)
                .stream()
                .map(itemRequestMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        User requestor = userRepository.findUserById(userId);
        if (requestor == null) {
            throw new UserNotFoundException(userId);
        }
        if (from == null || size == null) {
            return itemRequestRepository
                    .getAllItemRequests(requestor)
                    .stream()
                    .map(itemRequestMapper::convert)
                    .collect(Collectors.toList());
        }
        Pageable pageable = pageBuilder.build(from, size, requestsSort);
        return itemRequestRepository
                .getAllItemRequests(requestor, pageable)
                .stream()
                .map(itemRequestMapper::convert)
                .collect(Collectors.toList());
    }
}
