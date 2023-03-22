package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.dto.mapper.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        ItemMapper.class,
        ItemDtoMapper.class,
        ItemToSimpleDtoMapper.class,
        CommentDtoMapper.class,
        CommentMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {

    private final Converter<Item, ItemDto> itemMapper;

    private final Converter<ItemDto, Item> itemDtoMapper;

    private final Converter<Item, ItemSimpleDto> itemToSimpleDtoMapper;

    private final Converter<Comment, CommentDto> commentMapper;

    private final Converter<CommentDto, Comment> commentDtoMapper;

    private final User user = new User(
            1L,
            "test_name",
            "test_email"
    );

    private final Item item = new Item(
            1L,
            "name",
            "description",
            true,
            null,
            null
    );

    private final ItemDto itemDto = new ItemDto(
            1L,
            "name",
            "description",
            true,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final ItemSimpleDto itemSimpleDto = new ItemSimpleDto(
            1L,
            "name",
            "description",
            true,
            null
    );

    private final Comment comment = new Comment(
            1L,
            "text",
            user,
            item,
            LocalDateTime.MIN
    );

    private final CommentDto commentDto = new CommentDto(
            1L,
            "text",
            user.getName(),
            LocalDateTime.MIN
    );

    @Test
    @DisplayName("Item entity mapper test")
    void itemEntityMapperTest() throws Exception {
        assertThat(itemMapper.convert(item)).isEqualTo(itemDto);
    }

    @Test
    @DisplayName("Item dto entity mapper test")
    void itemDtoMapperTest() throws Exception {
        assertThat(itemDtoMapper.convert(itemDto).withId(1L)).isEqualTo(item);
    }

    @Test
    @DisplayName("Comment entity mapper test")
    void commentEntityMapperTest() throws Exception {
        assertThat(commentMapper.convert(comment)).isEqualTo(commentDto);
    }

    @Test
    @DisplayName("Comment dto entity mapper test")
    void commentDtoMapperTest() throws Exception {
        assertThat(commentDtoMapper.convert(commentDto).withId(1L)).isEqualTo(comment);
    }

    @Test
    @DisplayName("Item to simple dto mapper test")
    void itemToSimpleDtoMapperTest() throws Exception {
        assertThat(itemToSimpleDtoMapper.convert(item)).isEqualTo(itemSimpleDto);
    }
}
