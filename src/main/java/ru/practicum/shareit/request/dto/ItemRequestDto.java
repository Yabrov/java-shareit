package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemSimpleDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class ItemRequestDto {

    @JsonProperty("id")
    private Long id;

    @NotNull
    @NotEmpty
    @JsonProperty("description")
    private String description;

    @JsonProperty("created")
    private LocalDateTime created;

    @JsonProperty("items")
    private Collection<ItemSimpleDto> items;
}
