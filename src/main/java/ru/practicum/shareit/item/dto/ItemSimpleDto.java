package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemSimpleDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("available")
    private Boolean available;

    @JsonProperty("requestId")
    private Long requestId;

    public ItemSimpleDto withId(Long id) {
        ItemSimpleDto itemDto = (ItemSimpleDto) SerializationUtils.clone(this);
        itemDto.setId(id);
        return itemDto;
    }

    public ItemSimpleDto withName(String name) {
        ItemSimpleDto itemDto = (ItemSimpleDto) SerializationUtils.clone(this);
        itemDto.setName(name);
        return itemDto;
    }

    public ItemSimpleDto withDescription(String description) {
        ItemSimpleDto itemDto = (ItemSimpleDto) SerializationUtils.clone(this);
        itemDto.setDescription(description);
        return itemDto;
    }

    public ItemSimpleDto withAvailable(Boolean available) {
        ItemSimpleDto itemDto = (ItemSimpleDto) SerializationUtils.clone(this);
        itemDto.setAvailable(available);
        return itemDto;
    }

    public ItemSimpleDto withRequestId(Long requestId) {
        ItemSimpleDto itemDto = (ItemSimpleDto) SerializationUtils.clone(this);
        itemDto.setRequestId(requestId);
        return itemDto;
    }
}
