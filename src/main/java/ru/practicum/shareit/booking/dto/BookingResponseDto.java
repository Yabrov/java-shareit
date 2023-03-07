package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
public class BookingResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("start")
    private LocalDateTime start;

    @JsonProperty("end")
    private LocalDateTime end;

    @JsonProperty("status")
    private BookingStatus status;

    @JsonProperty("item")
    private ItemDto itemDto;

    @JsonProperty("booker")
    private UserDto bookerDto;
}
