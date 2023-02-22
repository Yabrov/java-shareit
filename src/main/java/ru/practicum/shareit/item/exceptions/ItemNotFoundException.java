package ru.practicum.shareit.item.exceptions;

public class ItemNotFoundException extends RuntimeException {

    private static final String MES_PATTERN = "Item with id %s does not exist.";

    public ItemNotFoundException(Integer id) {
        super(String.format(MES_PATTERN, id));
    }
}
