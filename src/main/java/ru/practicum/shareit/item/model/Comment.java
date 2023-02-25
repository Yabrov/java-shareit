package ru.practicum.shareit.item.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity<Long> {

    private String text;
    private User author;
    private Item item;
    private LocalDateTime created;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Column(
            name = "text",
            nullable = false,
            columnDefinition = "text"
    )
    public String getText() {
        return text;
    }

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    public User getAuthor() {
        return author;
    }

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    public Item getItem() {
        return item;
    }

    @Column(
            name = "created",
            nullable = false,
            columnDefinition = "timestamp"
    )
    @CreationTimestamp
    public LocalDateTime getCreated() {
        return created;
    }
}
