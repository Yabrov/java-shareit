package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(
        name = "comments",
        indexes = {@Index(name = "comment_item_id_idx", columnList = "item_id")}
)
public class Comment extends BaseEntity<Long> {

    public Comment(Long id,
                   String text,
                   User author,
                   Item item,
                   LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.item = item;
        this.created = created;
    }

    @Column(
            name = "text",
            nullable = false,
            columnDefinition = "text"
    )
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(
            name = "created",
            nullable = false,
            columnDefinition = "timestamp"
    )
    @CreationTimestamp
    private LocalDateTime created;

    public Comment withId(Long id) {
        Comment comment = (Comment) SerializationUtils.clone(this);
        comment.setId(id);
        return comment;
    }

    public Comment withAuthor(User author) {
        Comment comment = (Comment) SerializationUtils.clone(this);
        comment.setAuthor(author);
        return comment;
    }

    public Comment withItem(Item item) {
        Comment comment = (Comment) SerializationUtils.clone(this);
        comment.setItem(item);
        return comment;
    }
}
