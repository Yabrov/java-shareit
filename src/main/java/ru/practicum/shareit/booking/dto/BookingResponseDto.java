package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class BookingResponseDto implements Serializable {

    private static final Long serialVersionUID = 2L;

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

    public BookingResponseDto withId(Long id) {
        BookingResponseDto bookingResponseDto = (BookingResponseDto) SerializationUtils.clone(this);
        bookingResponseDto.setId(id);
        return bookingResponseDto;
    }

    public BookingResponseDto withStatus(BookingStatus status) {
        BookingResponseDto bookingResponseDto = (BookingResponseDto) SerializationUtils.clone(this);
        bookingResponseDto.setStatus(status);
        return bookingResponseDto;
    }

    public BookingResponseDto withBookerDto(UserDto bookerDto) {
        BookingResponseDto bookingResponseDto = (BookingResponseDto) SerializationUtils.clone(this);
        bookingResponseDto.setBookerDto(bookerDto);
        return bookingResponseDto;
    }
}
