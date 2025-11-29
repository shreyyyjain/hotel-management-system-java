package com.shrey.hotel.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final List<Room> bookedRooms = new ArrayList<>();
    private final Map<TakeawayItem, Integer> takeawayItems = new HashMap<>();

    public void addRoom(Room room) {
        if (room == null) throw new IllegalArgumentException("room is null");
        bookedRooms.add(room);
    }

    public boolean removeRoom(Room room) { return bookedRooms.remove(room); }

    public void addTakeawayItem(TakeawayItem item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        takeawayItems.put(item, takeawayItems.getOrDefault(item, 0) + 1);
    }

    public boolean removeTakeawayItem(TakeawayItem item) {
        if (item == null) return false;
        int qty = takeawayItems.getOrDefault(item, 0);
        if (qty <= 1) {
            return takeawayItems.remove(item) != null;
        } else {
            takeawayItems.put(item, qty - 1);
            return true;
        }
    }

    public List<Room> getBookedRooms() { return Collections.unmodifiableList(bookedRooms); }
    
    public Map<TakeawayItem, Integer> getTakeawayItems() { 
        return Collections.unmodifiableMap(takeawayItems);
    }

    public java.math.BigDecimal getTotalBill() {
        BigDecimal total = BigDecimal.ZERO;
        for (Room r : bookedRooms) {
            total = total.add(r.getPricePerNight());
        }
        for (Map.Entry<TakeawayItem, Integer> entry : takeawayItems.entrySet()) {
            total = total.add(entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return total;
    }

    public void clear() {
        bookedRooms.clear();
        takeawayItems.clear();
    }
}
