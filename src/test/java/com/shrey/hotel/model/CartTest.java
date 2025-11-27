package com.shrey.hotel.model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void totalCalculatesCorrectly() {
        Cart cart = new Cart();
        Room r = new Room(1, "Std", BigDecimal.valueOf(1000));
        TakeawayItem t = new TakeawayItem("Pizza", "Italian", BigDecimal.valueOf(200));
        cart.addRoom(r);
        cart.addTakeawayItem(t);
        assertEquals(BigDecimal.valueOf(1200), cart.getTotalBill());
    }

    @Test
    void removeWorksAndTotalUpdates() {
        Cart cart = new Cart();
        Room r = new Room(1, "Std", BigDecimal.valueOf(1000));
        cart.addRoom(r);
        assertTrue(cart.removeRoom(r));
        assertEquals(BigDecimal.ZERO, cart.getTotalBill());
    }

    @Test
    void cannotAddNulls() {
        Cart cart = new Cart();
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> cart.addTakeawayItem(null));
        assertTrue(ex1.getMessage().contains("null") || ex1.getMessage().length() > 0);
        
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> cart.addRoom(null));
        assertTrue(ex2.getMessage().contains("null") || ex2.getMessage().length() > 0);
    }
}
