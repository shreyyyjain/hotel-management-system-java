package com.shrey.hotel.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {
    private final List<Room> bookedRooms = new ArrayList<>();
    private final List<TakeawayItem> takeawayItems = new ArrayList<>();

    public void addRoom(Room room) {
        if (room == null) throw new IllegalArgumentException("room is null");
        bookedRooms.add(room);
    }

    public boolean removeRoom(Room room) { return bookedRooms.remove(room); }

    public void addTakeawayItem(TakeawayItem item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        takeawayItems.add(item);
    }

    public boolean removeTakeawayItem(TakeawayItem item) { return takeawayItems.remove(item); }

    public List<Room> getBookedRooms() { return Collections.unmodifiableList(bookedRooms); }
    public List<TakeawayItem> getTakeawayItems() { return Collections.unmodifiableList(takeawayItems); }

    public java.math.BigDecimal getTotalBill() {
        BigDecimal total = BigDecimal.ZERO;
        for (Room r : bookedRooms) {
            total = total.add(r.getPricePerNight());
        }
        for (TakeawayItem t : takeawayItems) {
            total = total.add(t.getPrice());
        }
        return total;
    }

    public void clear() {
        bookedRooms.clear();
        takeawayItems.clear();
    }
}
