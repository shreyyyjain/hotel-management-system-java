package com.shrey.hotel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer roomNumber;

    @Column(nullable = false)
    private String roomType; // Standard, Deluxe, Suite

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Boolean available = true;

    @Version
    private Long version; // Optimistic locking for concurrent bookings

    public synchronized Boolean bookRoom() {
        if (this.available) {
            this.available = false;
            return true;
        }
        return false;
    }

    public synchronized void releaseRoom() {
        this.available = true;
    }
}
