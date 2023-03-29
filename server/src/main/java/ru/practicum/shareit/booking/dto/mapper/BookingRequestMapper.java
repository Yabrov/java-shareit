package ru.practicum.shareit.booking.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class BookingRequestMapper implements Converter<BookingRequestDto, Booking> {

    @Override
    public Booking convert(BookingRequestDto source) {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(source.getItemId());
        booking.setItem(item);
        booking.setStart(source.getStart());
        booking.setEnd(source.getEnd());
        return booking;
    }
}
