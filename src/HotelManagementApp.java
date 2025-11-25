import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelManagementApp {
    private static final List<User> users = new ArrayList<>();
    private static User currentUser;
    private static final List<Room> rooms = new ArrayList<>();
    private static final List<TakeawayItem> takeawayMenu = new ArrayList<>();
    private static final Cart cart = new Cart();

    public static void main(String[] args) {
        System.out.println("Welcome to Hotel Management System!");
        // Dummy data initialization
        initializeDummyData();
        showLoginSignupGUI();
    }

    private static void initializeDummyData() {
        // Initialize rooms
        rooms.add(new Room(101, "Standard Room", 1500));
        rooms.add(new Room(102, "Deluxe Room", 2000));
        rooms.add(new Room(103, "Suite", 5000));

        // Initialize takeaway menu with items from various cuisines
        takeawayMenu.add(new TakeawayItem("Butter Chicken", "Indian", 250));
        takeawayMenu.add(new TakeawayItem("Masala Dosa", "Indian", 180));

        takeawayMenu.add(new TakeawayItem("Margherita Pizza", "Italian", 300));
        takeawayMenu.add(new TakeawayItem("Pasta Carbonara", "Italian", 280));

        takeawayMenu.add(new TakeawayItem("Sushi Rolls", "Japanese", 350));
        takeawayMenu.add(new TakeawayItem("Ramen Noodles", "Japanese", 220));

        takeawayMenu.add(new TakeawayItem("Pad Thai", "Thai", 280));
        takeawayMenu.add(new TakeawayItem("Green Curry", "Thai", 320));

        takeawayMenu.add(new TakeawayItem("Kung Pao Chicken", "Chinese", 240));
        takeawayMenu.add(new TakeawayItem("Chow Mein", "Chinese", 200));

        takeawayMenu.add(new TakeawayItem("Grilled Salmon", "Norwegian", 400));
        takeawayMenu.add(new TakeawayItem("Fish Soup", "Norwegian", 280));

        // Add other common cuisines and their respective items
        takeawayMenu.add(new TakeawayItem("Chicken Tikka", "Middle Eastern", 220));
        takeawayMenu.add(new TakeawayItem("Falafel Wrap", "Middle Eastern", 180));

        takeawayMenu.add(new TakeawayItem("Cheeseburger", "American", 200));
        takeawayMenu.add(new TakeawayItem("Caesar Salad", "American", 180));

        // Initialize some default users
        users.add(new User("admin", "admin")); // Admin user
    }

    private static void showLoginSignupGUI() {
        JFrame loginSignupFrame = new JFrame("Login / Signup");
        loginSignupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginSignupFrame.setSize(300, 200);
        loginSignupFrame.setLayout(new GridLayout(4, 1));

        // Center the frame
        loginSignupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3)); // Increase grid layout columns to accommodate the exit button

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginSignupFrame.dispose();
                showLoginGUI();
            }
        });

        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginSignupFrame.dispose();
                showSignupGUI();
            }
        });

        JButton exitButton = new JButton("Exit"); // Create exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application when exit button is clicked
            }
        });

        panel.add(loginButton);
        panel.add(signupButton);
        panel.add(exitButton); // Add exit button to the panel

        loginSignupFrame.add(new JLabel("Welcome to Hotel Management System!"));
        loginSignupFrame.add(new JLabel("Choose an option:"));
        loginSignupFrame.add(panel);

        loginSignupFrame.setVisible(true);
    }

    private static void showLoginGUI() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation to exit
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 1));

        // Center the frame
        loginFrame.setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit"); // Add exit button

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean authenticated = authenticate(username, password);
                if (authenticated) {
                    loginFrame.dispose();
                    showMenuGUI();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() { // Add action listener for exit button
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application when exit button is clicked
            }
        });

        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.add(exitButton); // Add exit button to the frame

        loginFrame.setVisible(true);
    }

    private static void showSignupGUI() {
        JFrame signupFrame = new JFrame("Signup");
        signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signupFrame.setSize(300, 200);
        signupFrame.setLayout(new GridLayout(4, 1));

        // Center the frame
        signupFrame.setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Create a username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Create a password:");
        JPasswordField passwordField = new JPasswordField();
        JButton signupButton = new JButton("Signup");

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(signupFrame, "Username and password cannot be empty!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(signupFrame, "Username already exists!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    users.add(new User(username, password));
                    JOptionPane.showMessageDialog(signupFrame, "User signed up successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    signupFrame.dispose();
                    showLoginGUI();
                }
            }
        });

        signupFrame.add(usernameLabel);
        signupFrame.add(usernameField);
        signupFrame.add(passwordLabel);
        signupFrame.add(passwordField);
        signupFrame.add(signupButton);

        signupFrame.setVisible(true);
    }

    private static boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.authenticate(username, password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private static boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static JFrame menuFrame; // Declare menuFrame as a class-level variable

    private static void showMenuGUI() {
        // Check if there's already a menu window open
        if (menuFrame != null) {
            // Dispose of the existing menu window
            menuFrame.dispose();
        }

        // Create a new menu window
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridLayout(1, 2));

        // Center the frame
        frame.setLocationRelativeTo(null);

        // Calculate the center position for the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - frame.getWidth()) / 2;
        int centerY = (screenSize.height - frame.getHeight()) / 2;

        // Set the frame location
        frame.setLocation(centerX, centerY);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(0, 1));

        JButton bookRoomButton = new JButton("Book a Room");
        bookRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookRoomGUI();
            }
        });
        leftPanel.add(bookRoomButton);

        JButton takeawayMenuButton = new JButton("Order Takeaway");
        takeawayMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTakeawayMenuGUI();
            }
        });
        leftPanel.add(takeawayMenuButton);

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewCartGUI();
            }
        });
        leftPanel.add(viewCartButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });
        leftPanel.add(checkoutButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logging out...");
                currentUser = null;
                frame.dispose();
                showLoginGUI();
            }
        });
        leftPanel.add(logoutButton);

        frame.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1));

        frame.add(rightPanel);

        frame.setVisible(true);

        // Set the menuFrame to the newly created frame
        menuFrame = frame;
    }

    private static void bookRoomGUI() {
        JFrame bookRoomFrame = new JFrame("Book a Room");
        bookRoomFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bookRoomFrame.setSize(300, 200);
        bookRoomFrame.setLayout(new GridLayout(0, 1));

        // Calculate the center position for the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - bookRoomFrame.getWidth()) / 2;
        int centerY = (screenSize.height - bookRoomFrame.getHeight()) / 2;

        // Set the frame location
        bookRoomFrame.setLocation(centerX, centerY);

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            JLabel noRoomsLabel = new JLabel("No rooms are available.");
            bookRoomFrame.add(noRoomsLabel);
        } else {
            JLabel selectionLabel = new JLabel("Select a room:");
            bookRoomFrame.add(selectionLabel);

            int index = 1;

            for (Room room : availableRooms) {
                final int roomNumber = room.getRoomNumber();
                JButton roomButton = new JButton(roomNumber + ". " + room.getRoomType() + " - Rs." + room.getPrice());
                roomButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cart.addRoom(room);
                        room.book();
                        JOptionPane.showMessageDialog(bookRoomFrame, "Room booked successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        bookRoomFrame.dispose();
                    }
                });
                bookRoomFrame.add(roomButton);
                index++;
            }
        }

        bookRoomFrame.setVisible(true);
    }

    private static void showTakeawayMenuGUI() {
        // Create the frame
        JFrame takeawayMenuFrame = new JFrame("Takeaway Menu");
        takeawayMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        takeawayMenuFrame.setSize(600, 400);

        // Center the frame
        takeawayMenuFrame.setLocationRelativeTo(null);

        // Create a panel to hold the menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Create a map to group items by cuisine
        Map<String, List<TakeawayItem>> cuisineMap = new HashMap<>();

        // Group items by cuisine
        for (TakeawayItem item : takeawayMenu) {
            String cuisine = item.getCuisine();
            cuisineMap.computeIfAbsent(cuisine, k -> new ArrayList<>()).add(item);
        }

        // Display items grouped by cuisine
        for (Map.Entry<String, List<TakeawayItem>> entry : cuisineMap.entrySet()) {
            JLabel cuisineLabel = new JLabel(entry.getKey() + " Cuisine");
            cuisineLabel.setFont(new Font("Arial", Font.BOLD, 16));
            menuPanel.add(cuisineLabel);

            for (TakeawayItem item : entry.getValue()) {
                JLabel itemLabel = new JLabel(String.format("%-25s Rs. %d", item.getName(), item.getPrice()));
                menuPanel.add(itemLabel);

                // Add spacing between each item
                menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel to hold the button
                JButton addToCartButton = new JButton("Add to Cart");
                addToCartButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cart.addTakeawayItem(item);
                        JOptionPane.showMessageDialog(takeawayMenuFrame, "Item added to cart successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                buttonPanel.add(addToCartButton);
                menuPanel.add(buttonPanel);

                // Add extra space after the button
                menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            // Add extra space after each cuisine
            menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Add the menu panel to the frame
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        takeawayMenuFrame.add(scrollPane);

        // Show the frame
        takeawayMenuFrame.setVisible(true);
    }

    private static JFrame cartFrame; // Declare cartFrame as a class-level variable
    private static JTextArea billTextArea; // Declare billTextArea as a class-level variable

    private static void viewCartGUI() {
        // Check if there's already a cart window open
        if (cartFrame != null) {
            // Dispose of the existing cart window
            cartFrame.dispose();
        }

        // Create a new cart window
        cartFrame = new JFrame("Cart");
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cartFrame.setSize(400, 300);
        cartFrame.setLayout(new GridLayout(0, 1));

        // Center the frame
        cartFrame.setLocationRelativeTo(null);

        List<Room> bookedRooms = cart.getBookedRooms();
        List<TakeawayItem> takeawayItems = cart.getTakeawayItems();

        for (Room room : bookedRooms) {
            JLabel roomLabel = new JLabel("Room " + room.getRoomNumber() + ": Rs." + room.getPrice());
            cartFrame.add(roomLabel);

            JButton removeRoomButton = new JButton("Remove Room");
            removeRoomButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart.removeRoom(room);
                    JOptionPane.showMessageDialog(cartFrame, "Room removed successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewCartGUI(); // Refresh the cart window after removing the room
                }
            });
            removeRoomButton.setPreferredSize(new Dimension(100, 30)); // Set button size
            cartFrame.add(removeRoomButton);
        }

        for (TakeawayItem item : takeawayItems) {
            JLabel itemLabel = new JLabel(item.getName() + ": Rs." + item.getPrice());
            cartFrame.add(itemLabel);

            JButton removeItemButton = new JButton("Remove Item");
            removeItemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cart.removeTakeawayItem(item);
                    JOptionPane.showMessageDialog(cartFrame, "Item removed successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    viewCartGUI(); // Refresh the cart window after removing the item
                }
            });
            removeItemButton.setPreferredSize(new Dimension(100, 30)); // Set button size
            cartFrame.add(removeItemButton);
        }

        JLabel totalLabel = new JLabel("Total Bill: Rs." + cart.getTotalBill());
        cartFrame.add(totalLabel);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkout(); // Call the checkout method
            }
        });
        cartFrame.add(checkoutButton);

        // Show the frame
        cartFrame.setVisible(true);
    }

    private static void updateBill() {
        StringBuilder billBuilder = new StringBuilder();
        List<Room> bookedRooms = cart.getBookedRooms();
        List<TakeawayItem> takeawayItems = cart.getTakeawayItems();

        billBuilder.append("Booked Rooms:\n");
        for (Room room : bookedRooms) {
            billBuilder.append("Room ").append(room.getRoomNumber()).append(": Rs.").append(room.getPrice())
                    .append("\n");
        }
        billBuilder.append("\n");

        billBuilder.append("Takeaway Items:\n");
        for (TakeawayItem item : takeawayItems) {
            billBuilder.append(item.getName()).append(": Rs.").append(item.getPrice()).append("\n");
        }
        billBuilder.append("\n");

        billBuilder.append("Total Bill: Rs.").append(cart.getTotalBill());

        billTextArea.setText(billBuilder.toString());
    }

    private static void checkout() {
        JFrame checkoutFrame = new JFrame("Checkout");
        checkoutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        checkoutFrame.setSize(300, 200);
        checkoutFrame.setLayout(new GridLayout(0, 1));

        // Calculate the center position for the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - checkoutFrame.getWidth()) / 2;
        int centerY = (screenSize.height - checkoutFrame.getHeight()) / 2;

        // Set the frame location
        checkoutFrame.setLocation(centerX, centerY);

        List<TakeawayItem> takeawayItems = cart.getTakeawayItems();
        List<Room> bookedRooms = cart.getBookedRooms();

        if (takeawayItems.isEmpty() && bookedRooms.isEmpty()) {
            JLabel emptyCartLabel = new JLabel("Your cart is empty.");
            checkoutFrame.add(emptyCartLabel);
            // JOptionPane.showMessageDialog(checkoutFrame, "Your cart is empty!", "Error",
            // JOptionPane.INFORMATION_MESSAGE);
        } else {
            JLabel totalLabel = new JLabel("Total Bill: Rs." + cart.getTotalBill());
            checkoutFrame.add(totalLabel);

            JButton confirmCheckoutButton = new JButton("Confirm Checkout");
            confirmCheckoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Process payment here
                    JLabel paymentLabel = new JLabel("Payment processed successfully!");
                    checkoutFrame.add(paymentLabel);
                    JOptionPane.showMessageDialog(checkoutFrame, "Payment processed successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    cart.clearCart();
                    checkoutFrame.dispose();
                    // Close the cart window and return to the homepage
                    cartFrame.dispose();
                    showMenuGUI();
                }
            });
            checkoutFrame.add(confirmCheckoutButton);
        }

        // Show the frame
        checkoutFrame.setVisible(true);
    }

}

class Room {
    private final int roomNumber;
    private final String roomType;
    private final double price;
    private boolean isAvailable;

    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void book() {
        isAvailable = false;
    }

    public void release() {
        isAvailable = true;
    }
}

class TakeawayItem {
    private final String name;
    private final String cuisine;
    private final int price;

    public TakeawayItem(String name, String cuisine, int price) {
        this.name = name;
        this.cuisine = cuisine;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public int getPrice() {
        return price;
    }
}

class Cart {
    private final List<Room> bookedRooms;
    private final List<TakeawayItem> takeawayItems;

    public Cart() {
        bookedRooms = new ArrayList<>();
        takeawayItems = new ArrayList<>();
    }

    public void addRoom(Room room) {
        bookedRooms.add(room);
    }

    public void addTakeawayItem(TakeawayItem item) {
        takeawayItems.add(item);
    }

    public void removeRoom(Room room) {
        bookedRooms.remove(room);
    }

    public void removeTakeawayItem(TakeawayItem item) {
        takeawayItems.remove(item);
    }

    public List<Room> getBookedRooms() {
        return Collections.unmodifiableList(bookedRooms);
    }

    public List<TakeawayItem> getTakeawayItems() {
        return Collections.unmodifiableList(takeawayItems);
    }

    public double getTotalBill() {
        double totalBill = bookedRooms.stream().mapToDouble(Room::getPrice).sum();
        totalBill += takeawayItems.stream().mapToDouble(TakeawayItem::getPrice).sum();
        return totalBill;
    }

    // Method to clear the cart
    public void clearCart() {
        bookedRooms.clear(); // Clear the list of booked rooms
        takeawayItems.clear(); // Clear the list of takeaway items
    }
}

class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}