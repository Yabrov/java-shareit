package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@NotNull
@AllArgsConstructor
public class UserDto {

    @With
    @JsonProperty("id")
    private Long id;

    @With
    @JsonProperty("name")
    private String name;

    @With
    @Email
    @NotNull
    @JsonProperty("email")
    private String email;
}
