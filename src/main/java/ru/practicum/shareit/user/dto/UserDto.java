package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@With(onMethod_ = @SuppressWarnings(value = {"RC_REF_COMPARISON", "ES_COMPARING_PARAMETER_STRING_WITH_EQ"}))
@Getter
@NotNull
@AllArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Email
    @NotNull
    @JsonProperty("email")
    private String email;
}
