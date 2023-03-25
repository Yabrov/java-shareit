package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestRepositoryImpl implements ItemRequestRepository {

    private final JpaItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public ItemRequest getItemRequest(Long requestId) {
        return itemRequestRepository.findById(requestId).orElse(null);
    }

    @Override
    public Collection<ItemRequest> getItemRequestsByOwner(User owner, Sort sort) {
        return itemRequestRepository.findAllByRequestor(owner, sort);
    }

    @Override
    public Page<ItemRequest> getAllItemRequests(User requestor, Pageable pageable) {
        return itemRequestRepository.findAllByRequestorIsNot(requestor, pageable);
    }

    @Override
    public Collection<ItemRequest> getAllItemRequests(User requestor) {
        return itemRequestRepository.findAllByRequestorIsNot(requestor);
    }
}
