package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import ru.practicum.shareit.item.dto.ItemSimpleDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created")
    private LocalDateTime created;

    @JsonProperty("items")
    private Collection<ItemSimpleDto> items;

    public ItemRequestDto withId(Long id) {
        ItemRequestDto requestDto = (ItemRequestDto) SerializationUtils.clone(this);
        requestDto.setId(id);
        return requestDto;
    }

    public ItemRequestDto withCreated(LocalDateTime created) {
        ItemRequestDto requestDto = (ItemRequestDto) SerializationUtils.clone(this);
        requestDto.setCreated(created);
        return requestDto;
    }
}
