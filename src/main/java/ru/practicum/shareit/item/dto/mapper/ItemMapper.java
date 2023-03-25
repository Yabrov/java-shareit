package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

@Component
public class ItemMapper implements Converter<Item, ItemDto> {

    @Override
    public ItemDto convert(Item source) {
        return source != null
                ? new ItemDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getAvailable(),
                source.getRequest() != null ? source.getRequest().getId() : null,
                null,
                null,
                new ArrayList<>())
                : null;
    }
}
