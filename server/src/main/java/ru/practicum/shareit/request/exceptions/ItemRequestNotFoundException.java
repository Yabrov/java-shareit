package ru.practicum.shareit.request.exceptions;

public class ItemRequestNotFoundException extends RuntimeException {

    private static final String MES_PATTERN = "Item request with id %s does not exist.";

    public ItemRequestNotFoundException(Long requestId) {
        super(String.format(MES_PATTERN, requestId));
    }
}
