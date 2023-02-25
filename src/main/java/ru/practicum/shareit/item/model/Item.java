package ru.practicum.shareit.item.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "items")
public class Item extends BaseEntity<Long> {

    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private List<Comment> comments = new ArrayList<>();

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

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    public String getDescription() {
        return description;
    }

    @Column(name = "is_available", nullable = false)
    public Boolean getAvailable() {
        return available;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    public User getOwner() {
        return owner;
    }

    @ManyToOne
    @JoinColumn(name = "request_id")
    public ItemRequest getRequest() {
        return request;
    }

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public List<Comment> getComments() {
        return comments;
    }
}
