package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper implements Converter<Comment, CommentDto> {

    @Override
    public CommentDto convert(Comment source) {
        return new CommentDto(
                source.getId(),
                source.getText(),
                source.getAuthor() != null ? source.getAuthor().getName() : null,
                source.getCreated()
        );
    }
}
