package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(
        name = "bookings",
        indexes = {@Index(name = "booking_item_id_idx", columnList = "item_id")}
)
public class Booking extends BaseEntity<Long> {

    @Column(
            name = "start_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    private LocalDateTime start;

    @Column(
            name = "end_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Column(
            name = "status",
            length = 10,
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING;
}
