package com.shrey.hotel.service;

import java.util.List;
import java.util.Optional;

import com.shrey.hotel.model.Room;

public class BookingService {
    private final List<Room> rooms;

    public BookingService(List<Room> rooms) { this.rooms = rooms; }

    public Optional<Room> findAvailableRoom() {
        return rooms.stream().filter(Room::isAvailable).findFirst();
    }

    public boolean bookRoom(int roomNumber) {
        return rooms.stream()
            .filter(r -> r.getRoomNumber() == roomNumber)
            .findFirst()
            .map(Room::book)
            .orElse(false);
    }

    public void releaseRoom(int roomNumber) {
        rooms.stream().filter(r -> r.getRoomNumber() == roomNumber)
            .findFirst()
            .ifPresent(Room::release);
    }
}
