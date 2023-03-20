package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.item.dto.mapper.ItemToSimpleDtoMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        ItemRequestMapper.class,
        ItemRequestDtoMapper.class,
        ItemToSimpleDtoMapper.class,
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {

    private final Converter<ItemRequest, ItemRequestDto> requestMapper;

    private final Converter<ItemRequestDto, ItemRequest> requestDtoMapper;

    private final ItemRequest itemRequest = new ItemRequest(
            "test_description",
            null,
            LocalDateTime.of(2043, 1, 1, 11, 0, 0)
    ).withId(1L);

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "test_description",
            LocalDateTime.of(2043, 1, 1, 11, 0, 0),
            Collections.emptyList()
    );

    @Test
    @DisplayName("Item request entity mapper test")
    void itemRequestEntityMapperTest() throws Exception {
        assertThat(requestMapper.convert(itemRequest)).isEqualTo(itemRequestDto);
    }

    @Test
    @DisplayName("Item request dto mapper test")
    void itemRequestDtoMapperTest() throws Exception {
        assertThat(requestDtoMapper.convert(itemRequestDto).withId(1L)).isEqualTo(itemRequest);
    }
}
