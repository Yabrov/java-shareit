package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookingLinkedDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("bookerId")
    private Long bookerId;
}
