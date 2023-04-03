package ru.practicum.shareit.item.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentDtoMapper implements Converter<CommentDto, Comment> {

    @Override
    public Comment convert(CommentDto source) {
        Comment comment = new Comment();
        comment.setText(source.getText());
        return comment;
    }
}
