package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import ru.practicum.shareit.booking.dto.BookingLinkedDto;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDto implements Serializable {

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

    @Setter
    @JsonProperty("lastBooking")
    private BookingLinkedDto lastBooking;

    @Setter
    @JsonProperty("nextBooking")
    private BookingLinkedDto nextBooking;

    @JsonProperty("comments")
    private Collection<CommentDto> comments;

    public ItemDto withId(Long id) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setId(id);
        return itemDto;
    }

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

    public ItemDto withNextBooking(BookingLinkedDto nextBooking) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }

    public ItemDto withLastBooking(BookingLinkedDto lastBooking) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setLastBooking(lastBooking);
        return itemDto;
    }

    public ItemDto withComments(Collection<CommentDto> comments) {
        ItemDto itemDto = (ItemDto) SerializationUtils.clone(this);
        itemDto.setComments(comments);
        return itemDto;
    }
}
