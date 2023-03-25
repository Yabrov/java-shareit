package ru.practicum.shareit.request.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class ItemRequestDtoMapper implements Converter<ItemRequestDto, ItemRequest> {

    @Override
    public ItemRequest convert(ItemRequestDto source) {
        ItemRequest request = new ItemRequest();
        request.setDescription(source.getDescription());
        return request;
    }
}
