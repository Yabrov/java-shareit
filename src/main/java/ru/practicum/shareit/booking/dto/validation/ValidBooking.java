package ru.practicum.shareit.booking.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookingDtoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBooking {

    String message() default "date end must be after date start";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
