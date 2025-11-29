package com.shrey.hotel.repository;

import com.shrey.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(Integer roomNumber);
    List<Room> findByRoomType(String roomType);
    List<Room> findByAvailableTrue();
    Optional<Room> findFirstByAvailableTrue();
}
