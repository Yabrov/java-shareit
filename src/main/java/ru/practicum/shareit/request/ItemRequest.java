package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(
        name = "requests",
        indexes = {@Index(name = "requests_owner_id_idx", columnList = "requestor_id")}
)
public class ItemRequest extends BaseEntity<Long> {

    public ItemRequest(String description,
                       User requestor,
                       LocalDateTime created) {
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "text"
    )
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @Column(
            name = "created",
            nullable = false,
            columnDefinition = "timestamp"
    )
    @CreationTimestamp
    private LocalDateTime created;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public ItemRequest withId(Long id) {
        ItemRequest itemRequest = (ItemRequest) SerializationUtils.clone(this);
        itemRequest.setId(id);
        return itemRequest;
    }

    public ItemRequest withDescription(String description) {
        ItemRequest itemRequest = (ItemRequest) SerializationUtils.clone(this);
        itemRequest.setDescription(description);
        return itemRequest;
    }

    public ItemRequest withRequestor(User requestor) {
        ItemRequest itemRequest = (ItemRequest) SerializationUtils.clone(this);
        itemRequest.setRequestor(requestor);
        return itemRequest;
    }

    public ItemRequest withCreated(LocalDateTime created) {
        ItemRequest itemRequest = (ItemRequest) SerializationUtils.clone(this);
        itemRequest.setCreated(created);
        return itemRequest;
    }
}
