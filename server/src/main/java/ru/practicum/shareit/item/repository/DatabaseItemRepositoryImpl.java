package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.JpaBookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class DatabaseItemRepositoryImpl implements ItemRepository {

    private final JpaItemRepository itemRepository;
    private final JpaBookingRepository bookingRepository;
    private final JpaCommentRepository commentRepository;

    public DatabaseItemRepositoryImpl(
            @Lazy JpaItemRepository itemRepository,
            @Lazy JpaBookingRepository bookingRepository,
            @Lazy JpaCommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Transactional
    @Override
    public Item deleteItem(Long itemId) {
        Item deletedItem = itemRepository.findById(itemId).orElse(null);
        if (deletedItem != null) {
            itemRepository.deleteById(itemId);
        }
        return deletedItem;
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Collection<Item> findAllItems(Long userId) {
        User owner = new User();
        owner.setId(userId);
        return itemRepository.findAllByOwner(owner);
    }

    @Override
    public Page<Item> findAllItems(Long userId, Pageable pageable) {
        User owner = new User();
        owner.setId(userId);
        return itemRepository.findAllByOwner(owner, pageable);
    }

    @Override
    public Collection<Item> searchItem(String text) {
        return itemRepository.searchItems(text);
    }

    @Override
    public Page<Item> searchItem(String text, Pageable pageable) {
        return itemRepository.searchItems(text, pageable);
    }

    @Override
    public Booking getNextBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItem_IdAndStartIsAfterAndStatusOrderByStart(
                itemId,
                LocalDateTime.now(),
                BookingStatus.APPROVED
        );
    }

    @Override
    public Booking getLastBookingByItemId(Long itemId) {
        return bookingRepository.findFirstByItem_IdAndStartIsBeforeAndStatusOrderByEndDesc(
                itemId,
                LocalDateTime.now(),
                BookingStatus.APPROVED
        );
    }

    @Override
    public Boolean isUserRealBookerOfItem(Long itemId, Long userId) {
        return bookingRepository.countByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId,
                userId,
                BookingStatus.APPROVED,
                LocalDateTime.now()
        ) > 0;
    }

    @Override
    public Boolean isUserCommentatorOfItem(Long itemId, Long userId) {
        return commentRepository.countAllByItem_IdAndAuthor_Id(itemId, userId) > 0;
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Collection<Comment> findAllItemComments(Long itemId) {
        return commentRepository.findAllByItem_IdOrderByCreatedDesc(itemId);
    }
}
