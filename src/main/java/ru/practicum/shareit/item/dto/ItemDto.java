package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NotNull
@AllArgsConstructor
public class ItemDto {

    @JsonProperty("id")
    private Long id;

    @NotNull
    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotNull
    @NotEmpty
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("available")
    private Boolean available;

    @JsonProperty("requestId")
    private Long requestId;
}
