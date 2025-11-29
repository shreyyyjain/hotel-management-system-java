package com.shrey.hotel.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shrey.hotel.model.Room;
import com.shrey.hotel.repository.RoomRepository;
import com.shrey.hotel.service.BookingService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepository roomRepository;
    private final BookingService bookingService;

    public RoomController(RoomRepository roomRepository, BookingService bookingService) {
        this.roomRepository = roomRepository;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable long id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable String type) {
        return ResponseEntity.ok(roomRepository.findByRoomType(type));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        return ResponseEntity.ok(roomRepository.findByAvailableTrue());
    }

    @PostMapping("/{roomNumber}/book")
    public ResponseEntity<?> bookRoom(@PathVariable Integer roomNumber) {
        boolean success = bookingService.bookRoom(roomNumber);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Room booked successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Room not available"));
        }
    }

    @PostMapping("/{roomNumber}/release")
    public ResponseEntity<?> releaseRoom(@PathVariable Integer roomNumber) {
        bookingService.releaseRoom(roomNumber);
        return ResponseEntity.ok(Map.of("success", true, "message", "Room released"));
    }
}
