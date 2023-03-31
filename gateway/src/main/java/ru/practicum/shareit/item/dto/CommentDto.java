package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NotNull
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CommentDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @Getter
    @NotNull
    @NotEmpty
    @JsonProperty("text")
    private String text;

    @JsonProperty("authorName")
    private String authorName;

    @JsonProperty("created")
    private LocalDateTime created;

    public CommentDto withText(String text) {
        CommentDto commentDto = (CommentDto) SerializationUtils.clone(this);
        commentDto.setText(text);
        return commentDto;
    }
}
