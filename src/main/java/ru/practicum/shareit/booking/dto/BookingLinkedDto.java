package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

@Setter
@AllArgsConstructor
public class BookingLinkedDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bookerId")
    private Long bookerId;

    public BookingLinkedDto withId(Long id) {
        BookingLinkedDto bookingLinkedDto = (BookingLinkedDto) SerializationUtils.clone(this);
        bookingLinkedDto.setId(id);
        return bookingLinkedDto;
    }

    public BookingLinkedDto withBookkerId(Long bookerId) {
        BookingLinkedDto bookingLinkedDto = (BookingLinkedDto) SerializationUtils.clone(this);
        bookingLinkedDto.setBookerId(bookerId);
        return bookingLinkedDto;
    }
}
