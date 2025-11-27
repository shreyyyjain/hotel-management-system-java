package com.shrey.hotel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.shrey.hotel.gui.ModernLoginSignupGUI;
import com.shrey.hotel.model.Cart;
import com.shrey.hotel.model.Room;
import com.shrey.hotel.model.TakeawayItem;
import com.shrey.hotel.model.User;
import com.shrey.hotel.service.BookingService;
import com.shrey.hotel.util.HashUtil;

public class App {

    private final List<User> users = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<TakeawayItem> menu = new ArrayList<>();
    private final Cart cart = new Cart();
    private final BookingService bookingService;

    public App() {
        initializeDummyData();
        bookingService = new BookingService(rooms);
    }

    public void start() {
        // Launch GUI (Login / Signup)
        ModernLoginSignupGUI gui = new ModernLoginSignupGUI(users, cart, rooms, menu, bookingService);
        gui.show();
    }

    private void initializeDummyData() {
        // 15 Standard Rooms
        for (int i = 1; i <= 15; i++) {
            rooms.add(new Room(100 + i, "Standard Room", BigDecimal.valueOf(1500)));
        }
        // 5 Deluxe Rooms
        for (int i = 1; i <= 5; i++) {
            rooms.add(new Room(200 + i, "Deluxe Room", BigDecimal.valueOf(2000)));
        }
        // 1 Suite
        rooms.add(new Room(301, "Suite", BigDecimal.valueOf(5000)));

        menu.add(new TakeawayItem("Butter Chicken", "Indian", BigDecimal.valueOf(250)));
        menu.add(new TakeawayItem("Masala Dosa", "Indian", BigDecimal.valueOf(180)));
        menu.add(new TakeawayItem("Margherita Pizza", "Italian", BigDecimal.valueOf(300)));
        menu.add(new TakeawayItem("Pasta Carbonara", "Italian", BigDecimal.valueOf(280)));
        menu.add(new TakeawayItem("Sushi Rolls", "Japanese", BigDecimal.valueOf(350)));
        menu.add(new TakeawayItem("Ramen Noodles", "Japanese", BigDecimal.valueOf(220)));
        menu.add(new TakeawayItem("Pad Thai", "Thai", BigDecimal.valueOf(280)));
        menu.add(new TakeawayItem("Green Curry", "Thai", BigDecimal.valueOf(320)));
        menu.add(new TakeawayItem("Kung Pao Chicken", "Chinese", BigDecimal.valueOf(240)));
        menu.add(new TakeawayItem("Chow Mein", "Chinese", BigDecimal.valueOf(200)));
        menu.add(new TakeawayItem("Grilled Salmon", "Norwegian", BigDecimal.valueOf(400)));
        menu.add(new TakeawayItem("Fish Soup", "Norwegian", BigDecimal.valueOf(280)));
        menu.add(new TakeawayItem("Chicken Tikka", "Middle Eastern", BigDecimal.valueOf(220)));
        menu.add(new TakeawayItem("Falafel Wrap", "Middle Eastern", BigDecimal.valueOf(180)));
        menu.add(new TakeawayItem("Cheeseburger", "American", BigDecimal.valueOf(200)));
        menu.add(new TakeawayItem("Caesar Salad", "American", BigDecimal.valueOf(180)));

        // default admin user (password: admin) - hashed
        users.add(new User("admin", HashUtil.hashPassword("admin")));
    }
}
