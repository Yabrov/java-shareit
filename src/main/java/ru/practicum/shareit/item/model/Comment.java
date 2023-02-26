package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "comments")
public class Comment extends BaseEntity<Long> {

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
}
