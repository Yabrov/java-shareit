package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemToSimpleDtoMapper implements Converter<Item, ItemSimpleDto> {

    @Override
    public ItemSimpleDto convert(Item source) {
        return new ItemSimpleDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getAvailable(),
                source.getRequest() != null ? source.getRequest().getId() : null
        );
    }
}
