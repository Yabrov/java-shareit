package ru.practicum.shareit.booking.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class BookingMapper implements Converter<Booking, BookingResponseDto> {

    private final Converter<Item, ItemDto> itemMapper;
    private final Converter<User, UserDto> userMapper;

    @Override
    public BookingResponseDto convert(Booking source) {
        return source != null
                ? new BookingResponseDto(
                source.getId(),
                source.getStart(),
                source.getEnd(),
                source.getStatus(),
                itemMapper.convert(source.getItem() != null ? source.getItem() : null),
                userMapper.convert(source.getBooker() != null ? source.getBooker() : null))
                : null;
    }
}
