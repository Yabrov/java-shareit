package ru.practicum.shareit.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ErrorResponse {

    @JsonProperty("error")
    String errorMes;
}
