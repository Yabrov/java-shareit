package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;
import ru.practicum.shareit.booking.dto.validation.ValidBookingRequest;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@With
@Getter
@NotNull
@ValidBookingRequest
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull
    private Long itemId;

    @Future
    @JsonProperty("start")
    private LocalDateTime start;

    @Future
    @JsonProperty("end")
    private LocalDateTime end;
}
