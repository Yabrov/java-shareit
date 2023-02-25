package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest extends BaseEntity<Long> {

    private User requestor;
    private String description;
    private LocalDateTime created;
    private List<Item> items = new ArrayList<>();

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
            name = "description",
            nullable = false,
            columnDefinition = "text"
    )
    public String getDescription() {
        return description;
    }

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    public User getRequestor() {
        return requestor;
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

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    private List<Item> getItems() {
        return items;
    }
}
