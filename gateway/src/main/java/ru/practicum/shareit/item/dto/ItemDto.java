package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@NotNull
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDto implements Serializable {

    private static final Long serialVersionUID = 2L;

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

    @JsonProperty("comments")
    private Collection<CommentDto> comments;

    public ItemDto withName(String name) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setName(name);
        return itemDto;
    }

    public ItemDto withDescription(String description) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setDescription(description);
        return itemDto;
    }

    public ItemDto withAvailable(Boolean available) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setAvailable(available);
        return itemDto;
    }
}
