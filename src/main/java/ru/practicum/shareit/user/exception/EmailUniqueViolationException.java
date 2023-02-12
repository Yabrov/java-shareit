package ru.practicum.shareit.user.exception;

public class EmailUniqueViolationException extends RuntimeException {

    private static final String MES_PATTERN = "Email '%s' already exists.";

    public EmailUniqueViolationException(String email) {
        super(String.format(MES_PATTERN, email));
    }
}
