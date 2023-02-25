package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "bookings")
public class Booking extends BaseEntity<Long> {

    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private Item item;
    private User booker;

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
            name = "start_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    public LocalDateTime getStart() {
        return start;
    }

    @Column(
            name = "end_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    public LocalDateTime getEnd() {
        return end;
    }

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    public Item getItem() {
        return item;
    }

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    public User getBooker() {
        return booker;
    }

    @Column(
            name = "status",
            length = 10,
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    public BookingStatus getStatus() {
        return status;
    }
}
