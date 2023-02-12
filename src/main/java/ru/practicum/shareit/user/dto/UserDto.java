package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDto {
    @JsonProperty("id") private Integer id;
    @JsonProperty("name") private String name;
    @JsonProperty("email") private String email;
}
