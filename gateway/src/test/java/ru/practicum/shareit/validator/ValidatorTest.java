package ru.practicum.shareit.validator;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ValidatorTest {

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            1L,
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0)
    );

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    Validator validator = factory.getValidator();

    @Test
    void validateValidBooking() {
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto);
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    void validateBookingWithNullStart() {
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto.withStart(null));
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void validateBookingWithNullEnd() {
        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingRequestDto.withEnd(null));
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    void validateBookingWithEndBeforeStart() {
        LocalDateTime start = LocalDateTime.of(2043, 1, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2043, 1, 1, 0, 0);
        Set<ConstraintViolation<BookingRequestDto>> violations = validator
                .validate(bookingRequestDto.withEnd(end).withStart(start));
        assertThat(violations.isEmpty()).isFalse();
    }
}
