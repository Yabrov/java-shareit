package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemDtoMapper implements Converter<ItemDto, Item> {

    @Override
    public Item convert(ItemDto source) {
        Item item = new Item();
        item.setId(null);
        item.setName(source.getName());
        item.setDescription(source.getDescription());
        item.setAvailable(source.getAvailable());
        item.setOwner(null);
        return item;
    }
}