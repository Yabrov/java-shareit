package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(concat('%', :pattern, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(concat('%', :pattern, '%'))")
    Collection<Item> searchItems(@Param("pattern") String text);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(concat('%', :pattern, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(concat('%', :pattern, '%'))")
    Page<Item> searchItems(@Param("pattern") String text, Pageable pageable);

    Collection<Item> findAllByOwner(User owner);

    Page<Item> findAllByOwner(User owner, Pageable pageable);
}
