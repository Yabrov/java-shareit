package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @Email
    @JsonProperty("email")
    private String email;
}
