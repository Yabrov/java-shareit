package ru.practicum.shareit.booking.dto.validation;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingDtoValidator implements ConstraintValidator<ValidBooking, BookingDto> {

    @Override
    public void initialize(ValidBooking constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto == null) {
            return false;
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return false;
        } else {
            return bookingDto.getStart().isBefore(bookingDto.getEnd());
        }
    }
}
