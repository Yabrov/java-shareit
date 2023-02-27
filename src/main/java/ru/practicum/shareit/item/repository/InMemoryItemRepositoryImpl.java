package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Set<Long>> owners = new HashMap<>();
    private Long currentId = 1L;

    public Item findItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item deleteItem(Long itemId) {
        Item deletedItem = items.remove(itemId);
        if (deletedItem != null) {
            owners.get(deletedItem.getOwner().getId()).remove(deletedItem.getId());
        }
        return deletedItem;
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(currentId++);
        items.put(item.getId(), item);
        Set<Long> ownerItems = owners.get(item.getOwner().getId());
        if (ownerItems == null) {
            ownerItems = new HashSet<>();
            ownerItems.add(item.getId());
            owners.put(item.getOwner().getId(), ownerItems);
        } else {
            ownerItems.add(item.getId());
        }
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.replace(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findAllItems(Long userId) {
        return owners
                .get(userId)
                .stream()
                .map(this::findItemById)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItem(String text) {
        return items
                .values()
                .stream()
                .filter(Item::getAvailable)
                .filter(getItemSerchPredicate(text))
                .collect(Collectors.toList());
    }

    private Predicate<Item> getItemSerchPredicate(String pattern) {
        return item -> (item.getName() + item.getDescription()).toLowerCase().contains(pattern.toLowerCase());
    }
}
