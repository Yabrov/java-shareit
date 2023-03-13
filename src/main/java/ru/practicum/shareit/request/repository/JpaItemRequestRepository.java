package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface JpaItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r LEFT JOIN FETCH r.items WHERE r.requestor = :requestor")
    Collection<ItemRequest> findAllByRequestor(User requestor, Sort sort);

    @Query("SELECT r FROM ItemRequest r LEFT JOIN FETCH r.items WHERE r.id = :requestId")
    Optional<ItemRequest> findItemRequestById(Long requestId);

    @Query(
            value = "SELECT r FROM ItemRequest r LEFT JOIN FETCH r.items WHERE r.requestor <> :user",
            countQuery = "SELECT COUNT(r) FROM ItemRequest r WHERE r.requestor <> :user"
    )
    Page<ItemRequest> findAllOthersItemRequests(User user, Pageable pageable);
}