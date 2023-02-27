package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class DatabaseItemRepositoryImpl implements ItemRepository {

    private final JpaItemRepository itemRepository;

    public DatabaseItemRepositoryImpl(@Lazy JpaItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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

    @Transactional
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
    public Collection<Item> searchItem(String text) {
        return itemRepository.searchItems(text);
    }
}