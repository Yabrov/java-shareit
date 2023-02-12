package ru.practicum.shareit.user.exception;

public class NullEmailException extends RuntimeException {

    private static final String MES_PATTERN = "Email must not be null.";

    public NullEmailException() {
        super(MES_PATTERN);
    }
}