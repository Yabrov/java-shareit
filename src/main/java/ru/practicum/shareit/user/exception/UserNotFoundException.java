package ru.practicum.shareit.user.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MES_PATTERN = "User with id %s does not exist.";

    public UserNotFoundException(Integer id) {
        super(String.format(MES_PATTERN, id));
    }
}
