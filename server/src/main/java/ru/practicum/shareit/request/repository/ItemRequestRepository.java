package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRequestRepository {

    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest getItemRequest(Long requestId);

    Collection<ItemRequest> getItemRequestsByOwner(User owner, Sort sort);

    Page<ItemRequest> getAllItemRequests(User requestor, Pageable pageable);

    Collection<ItemRequest> getAllItemRequests(User requestor);
}
