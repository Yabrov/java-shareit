package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, Set<Integer>> owners = new HashMap<>();
    private Integer currentId = 1;

    public Item findItemById(Integer itemId) {
        return items.get(itemId);
    }

    public Item deleteItem(Integer itemId) {
        Item deletedItem = findItemById(itemId);
        if (deletedItem != null) {
            items.remove(deletedItem.getId());
            owners.get(deletedItem.getOwner().getId()).remove(deletedItem.getId());
        }
        return deletedItem;
    }

    public Item saveItem(Item item) {
        item.setId(currentId++);
        items.put(item.getId(), item);
        Set<Integer> ownerItems = owners.get(item.getOwner().getId());
        if (ownerItems == null) {
            ownerItems = new HashSet<>();
            ownerItems.add(item.getId());
            owners.put(item.getOwner().getId(), ownerItems);
        } else {
            ownerItems.add(item.getId());
        }
        return item;
    }

    public Item updateItem(Item item) {
        items.replace(item.getId(), item);
        return item;
    }

    public Collection<Item> findAllItems(Integer userId) {
        return owners
                .get(userId)
                .stream()
                .map(this::findItemById)
                .collect(Collectors.toList());
    }

    public Collection<Item> searchItem(String text) {
        return items
                .values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName() + item.getDescription()).toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
