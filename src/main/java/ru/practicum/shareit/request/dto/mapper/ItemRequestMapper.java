package ru.practicum.shareit.request.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper implements Converter<ItemRequest, ItemRequestDto> {

    private final Converter<Item, ItemSimpleDto> itemSimpleDtoConverter;

    @Override
    public ItemRequestDto convert(ItemRequest source) {
        return new ItemRequestDto(
                source.getId(),
                source.getDescription(),
                source.getCreated(),
                source.getItems() == null
                        ? null
                        : source
                        .getItems()
                        .stream()
                        .map(itemSimpleDtoConverter::convert)
                        .collect(Collectors.toList())
        );
    }
}
