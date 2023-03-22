package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface JpaItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Collection<ItemRequest> findAllByRequestor(User requestor, Sort sort);

    Page<ItemRequest> findAllByRequestorIsNot(User user, Pageable pageable);

    Collection<ItemRequest> findAllByRequestorIsNot(User user);
}