package ru.practicum.shareit.booking.dto.validation;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingRequestValidator implements ConstraintValidator<ValidBookingRequest, BookingRequestDto> {

    @Override
    public void initialize(ValidBookingRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingRequestDto bookingRequestDto, ConstraintValidatorContext context) {
        if (bookingRequestDto == null) {
            return false;
        }
        if (bookingRequestDto.getStart() == null || bookingRequestDto.getEnd() == null) {
            return false;
        } else {
            return bookingRequestDto.getStart().isBefore(bookingRequestDto.getEnd());
        }
    }
}
