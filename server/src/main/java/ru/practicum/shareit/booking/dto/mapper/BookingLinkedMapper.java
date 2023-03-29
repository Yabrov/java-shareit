package ru.practicum.shareit.booking.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingLinkedDto;

@Component
public class BookingLinkedMapper implements Converter<Booking, BookingLinkedDto> {

    @Override
    public BookingLinkedDto convert(Booking source) {
        return new BookingLinkedDto(
                source.getId(),
                source.getBooker() != null ? source.getBooker().getId() : null
        );
    }
}
