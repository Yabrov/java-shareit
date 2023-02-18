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
        Item savingItem = item.withId(currentId++);
        items.put(savingItem.getId(), savingItem);
        Set<Integer> ownerItems = owners.get(savingItem.getOwner().getId());
        if (ownerItems == null) {
            ownerItems = new HashSet<>();
            ownerItems.add(savingItem.getId());
            owners.put(savingItem.getOwner().getId(), ownerItems);
        } else {
            ownerItems.add(savingItem.getId());
        }
        return savingItem;
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
