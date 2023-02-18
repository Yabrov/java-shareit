package ru.practicum.shareit.item.exceptions;

public class WrongItemOwnerException extends RuntimeException {

    private static final String MES_PATTERN = "User with id %s is not owner of item with id %s";

    public WrongItemOwnerException(Integer userId, Integer itemId) {
        super(String.format(MES_PATTERN, userId, itemId));
    }
}
