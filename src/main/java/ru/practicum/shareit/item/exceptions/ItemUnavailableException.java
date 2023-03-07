package ru.practicum.shareit.item.exceptions;

public class ItemUnavailableException extends RuntimeException {

    private static final String MES_PATTERN = "Item with id %s is not available.";

    public ItemUnavailableException(Long itemId) {
        super(String.format(MES_PATTERN, itemId));
    }
}
