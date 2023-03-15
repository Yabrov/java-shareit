package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

@Component
public class ItemDtoMapper implements Converter<ItemDto, Item> {

    @Override
    public Item convert(ItemDto source) {
        Item item = new Item();
        item.setId(null);
        item.setName(source.getName());
        item.setDescription(source.getDescription());
        item.setAvailable(source.getAvailable());
        if (source.getRequestId() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(source.getRequestId());
            item.setRequest(request);
        }
        item.setOwner(null);
        return item;
    }
}
