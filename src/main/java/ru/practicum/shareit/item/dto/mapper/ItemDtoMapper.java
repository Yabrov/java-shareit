package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper implements Converter<ItemDto, Item> {

    @Override
    public Item convert(ItemDto source) {
        return Item
                .builder()
                .id(null)
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .owner(null)
                .build();
    }
}
