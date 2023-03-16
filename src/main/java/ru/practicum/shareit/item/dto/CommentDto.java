package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.With;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@With
@NotNull
@AllArgsConstructor
public class CommentDto {

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
}
