package com.shrey.hotel.repository;

import com.shrey.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(Integer roomNumber);
    List<Room> findByRoomType(String roomType);
    List<Room> findByAvailableTrue();
    @Query("SELECT r FROM Room r WHERE r.available = true LIMIT 1")
    Optional<Room> findFirstAvailable();
}
