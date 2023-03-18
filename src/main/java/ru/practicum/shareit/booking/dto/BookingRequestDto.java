package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import ru.practicum.shareit.booking.dto.validation.ValidBookingRequest;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NotNull
@Getter
@Setter
@ValidBookingRequest
@AllArgsConstructor
@EqualsAndHashCode
public class BookingRequestDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @NotNull
    private Long itemId;

    @Future
    @JsonProperty("start")
    private LocalDateTime start;

    @Future
    @JsonProperty("end")
    private LocalDateTime end;

    public BookingRequestDto withItemId(Long itemId) {
        BookingRequestDto bookingRequestDto = (BookingRequestDto) SerializationUtils.clone(this);
        bookingRequestDto.setItemId(itemId);
        return bookingRequestDto;
    }

    public BookingRequestDto withStart(LocalDateTime start) {
        BookingRequestDto bookingRequestDto = (BookingRequestDto) SerializationUtils.clone(this);
        bookingRequestDto.setStart(start);
        return bookingRequestDto;
    }

    public BookingRequestDto withEnd(LocalDateTime end) {
        BookingRequestDto bookingRequestDto = (BookingRequestDto) SerializationUtils.clone(this);
        bookingRequestDto.setEnd(end);
        return bookingRequestDto;
    }
}
