package com.shrey.hotel.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Room {
    private final int roomNumber;
    private final String roomType;
    private final BigDecimal pricePerNight;
    private boolean occupied = false;

    public Room(int roomNumber, String roomType, BigDecimal pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return !occupied; }

    public synchronized boolean book() {
        if (occupied) return false;
        occupied = true;
        return true;
    }

    public synchronized void release() { occupied = false; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() { return Objects.hash(roomNumber); }
}
