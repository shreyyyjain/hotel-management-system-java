package com.shrey.hotel.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import com.shrey.hotel.model.Cart;
import com.shrey.hotel.model.Room;
import com.shrey.hotel.model.TakeawayItem;
import com.shrey.hotel.model.User;
import com.shrey.hotel.service.BookingService;
import com.shrey.hotel.util.HashUtil;

/**
 * Premium modern UI inspired by Airbnb, Booking.com, and modern fintech apps.
 * Features: smooth animations, professional typography, depth, and refined spacing.
 */
public class ModernLoginSignupGUI {
    private final List<User> users;
    private User currentUser;
    private final List<Room> rooms;
    private final List<TakeawayItem> takeawayMenu;
    private final Cart cart;
    private final BookingService bookingService;

    // Color palette (inspired by premium platforms)
    private static final Color PRIMARY = new Color(28, 107, 173); // Professional blue
    private static final Color SECONDARY = new Color(46, 204, 113); // Fresh green
    private static final Color ACCENT = new Color(230, 126, 34); // Warm orange
    private static final Color DARK_TEXT = new Color(45, 52, 54); // Dark gray
    private static final Color LIGHT_TEXT = new Color(127, 140, 141); // Light gray
    private static final Color BG_PRIMARY = new Color(255, 255, 255); // White
    private static final Color BG_SECONDARY = new Color(242, 245, 250); // Light blue-gray
    private static final Color BG_TERTIARY = new Color(236, 240, 241); // Subtle gray
    private static final Color ERROR = new Color(231, 76, 60); // Red

    private final JFrame mainFrame;
    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);
    private final Map<TakeawayItem, Integer> selectionMap = new HashMap<>();
    private final JButton floatingCartBtn = new JButton("Go to Cart 🛒");
    private JPanel cartPanel; // Store reference to cart panel for refresh

    public ModernLoginSignupGUI(List<User> users, Cart cart, List<Room> rooms, 
                                List<TakeawayItem> takeawayMenu, BookingService bookingService) {
        this.users = users;
        this.cart = cart;
        this.rooms = rooms;
        this.takeawayMenu = takeawayMenu;
        this.bookingService = bookingService;

        mainFrame = new JFrame("Premium Hotel Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(BG_SECONDARY);

        initializeCards();
        mainFrame.add(cardPanel, BorderLayout.CENTER);
    }

    public void show() {
        mainFrame.setVisible(true);
        cards.show(cardPanel, "auth");
    }

    private void initializeCards() {
        cardPanel.add(createAuthPanel(), "auth");
        cardPanel.add(createDashboardPanel(), "dashboard");
        cardPanel.add(createRoomsPanel(), "rooms");
        cardPanel.add(createFoodPanel(), "food");
        // Initialize cart panel
        cartPanel = createCartPanel();
        cardPanel.add(cartPanel, "cart");
    }

    // ============ AUTH PANEL (Login/Signup) ============
    private JPanel createAuthPanel() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), getHeight(), new Color(52, 152, 219));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);

        // Left side - branding
        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel logo = new JLabel("🏨");
        logo.setFont(new Font("Arial", Font.BOLD, 80));
        gbc.gridy = 0;
        left.add(logo, gbc);

        JLabel title = new JLabel("Luxe Hotels");
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(BG_PRIMARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        left.add(title, gbc);

        JLabel subtitle = new JLabel("Experience Premium Comfort");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(new Color(189, 195, 199));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 20, 20, 20);
        left.add(subtitle, gbc);

        // Right side - auth cards
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(BG_SECONDARY);
        right.setBorder(new EmptyBorder(40, 40, 40, 40));

        JTabbedPane tabs = new JTabbedPane(SwingConstants.TOP);
        tabs.setBackground(BG_PRIMARY);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel loginCard = createLoginCard();
        JPanel signupCard = createSignupCard();

        tabs.addTab("Login", loginCard);
        tabs.addTab("Sign Up", signupCard);

        // Style tabs
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setForegroundAt(i, LIGHT_TEXT);
        }

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        right.add(tabs, gbc);

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        return root;
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_PRIMARY);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;

        // Email field
        gbc.gridy = 0;
        card.add(createLabel("Email or Username"), gbc);
        gbc.gridy = 1;
        JTextField emailField = createModernTextField("Enter your email");
        card.add(emailField, gbc);

        // Password field (embedded eye icon)
        gbc.gridy = 2;
        card.add(createLabel("Password"), gbc);
        gbc.gridy = 3;
        JPasswordField passwordField = createModernPasswordField("Enter password");
        JPanel passWrapper = wrapPasswordFieldWithEye(passwordField);
        card.add(passWrapper, gbc);

        // Remember me
        gbc.gridy = 4;
        JCheckBox rememberMe = new JCheckBox("Remember me", true);
        rememberMe.setBackground(BG_PRIMARY);
        rememberMe.setForeground(LIGHT_TEXT);
        card.add(rememberMe, gbc);

        // Login button
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 10, 0);
        JButton loginBtn = createModernButton("Sign In", PRIMARY);
        loginBtn.addActionListener(e -> {
            String username = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (!isValidEmail(username)) {
                showErrorDialog("Invalid Email", "Please enter a valid email address.");
                return;
            }
            Optional<User> userOpt = users.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
            if (userOpt.isPresent() && userOpt.get().authenticate(password)) {
                currentUser = userOpt.get();
                cards.show(cardPanel, "dashboard");
            } else {
                showErrorDialog("Invalid credentials", "Please check your email and password.");
            }
        });
        card.add(loginBtn, gbc);

        // Forgot password link
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 0, 10, 0);
        JLabel forgotLink = new JLabel("<html><u>Forgot password?</u></html>");
        forgotLink.setForeground(PRIMARY);
        forgotLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(forgotLink, gbc);

        gbc.gridy = 7;
        gbc.weighty = 1;
        card.add(new JLabel(), gbc);

        return card;
    }

    private JPanel createSignupCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_PRIMARY);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1;

        // Full Name (required)
        gbc.gridy = 0;
        card.add(createLabel("Full Name *"), gbc);
        gbc.gridy = 1;
        JTextField nameField = createModernTextField("Enter your full name");
        card.add(nameField, gbc);

        // Email (require email format)
        gbc.gridy = 2;
        card.add(createLabel("Email"), gbc);
        gbc.gridy = 3;
        JTextField emailField = createModernTextField("Enter your email");
        card.add(emailField, gbc);

        // Password
        gbc.gridy = 4;
        card.add(createLabel("Password"), gbc);
        gbc.gridy = 5;
        JPasswordField passwordField = createModernPasswordField("Min 6 characters");
        JPanel pwWrap = wrapPasswordFieldWithEye(passwordField);
        card.add(pwWrap, gbc);

        // Confirm Password
        gbc.gridy = 6;
        card.add(createLabel("Confirm Password"), gbc);
        gbc.gridy = 7;
        JPasswordField confirmField = createModernPasswordField("Confirm password");
        JPanel confirmWrap = wrapPasswordFieldWithEye(confirmField);
        card.add(confirmWrap, gbc);

        // Terms (styled checkbox + clickable link)
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 0, 15, 0);
        JCheckBox termsBox = createStyledCheckBox();
        termsBox.setSelected(false);
        JLabel termsLink = new JLabel("<html>I agree to <u>Terms & Conditions</u></html>");
        termsLink.setForeground(PRIMARY);
        termsLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel termsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        termsRow.setBackground(BG_PRIMARY);
        termsRow.add(termsBox);
        termsRow.add(termsLink);
        termsLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTermsWindow(termsBox);
            }
        });
        card.add(termsRow, gbc);

        // Signup button
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 0, 10, 0);
        JButton signupBtn = createModernButton("Create Account", SECONDARY);
        signupBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (name.isEmpty()) {
                showErrorDialog("Validation Error", "Full name is required.");
                return;
            }
            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog("Validation Error", "Email and password are required.");
                return;
            }
            if (!isValidEmail(username)) {
                showErrorDialog("Invalid Email", "Please enter a valid email address.");
                return;
            }
            if (!password.equals(confirm)) {
                showErrorDialog("Password Mismatch", "Passwords do not match.");
                return;
            }
            if (password.length() < 6) {
                showErrorDialog("Weak Password", "Password must be at least 6 characters.");
                return;
            }
            if (!termsBox.isSelected()) {
                showErrorDialog("Terms Required", "You must agree to the Terms & Conditions to create an account.");
                return;
            }
            synchronized (users) {
                if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
                    showErrorDialog("Username Taken", "This email/username is already registered.");
                    return;
                }
                users.add(new User(username, HashUtil.hashPassword(password)));
            }
            currentUser = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
            cards.show(cardPanel, "dashboard");
        });
        card.add(signupBtn, gbc);

        gbc.gridy = 10;
        gbc.weighty = 1;
        card.add(new JLabel(), gbc);

        return card;
    }

    // ============ DASHBOARD PANEL ============
    private JPanel createDashboardPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_SECONDARY);

        // Header (dashboard shows hotel name, no home button)
        root.add(createHeaderWithHome(false), BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_SECONDARY);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Welcome card
        JPanel welcomeCard = createWelcomeCard();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        content.add(welcomeCard, gbc);

        // Quick actions
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.35;
        content.add(createActionCard("🏨", "Book a Room", () -> cards.show(cardPanel, "rooms")), gbc);

        gbc.gridx = 1;
        content.add(createActionCard("🍽️", "Order Food", () -> cards.show(cardPanel, "food")), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        content.add(createActionCard("🛒", "View Cart", () -> {
            // Refresh cart panel before showing
            refreshCartDisplay();
            cards.show(cardPanel, "cart");
        }), gbc);

        gbc.gridx = 1;
        content.add(createActionCard("⚙️", "Account", () -> showAccountDialog()), gbc);

        root.add(new JScrollPane(content), BorderLayout.CENTER);
        return root;
    }

    private JPanel createHeader() {
        return createHeaderWithHome(true);
    }

    private JPanel createHeaderWithHome(boolean showHome) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        left.setOpaque(false);

        if (showHome) {
            JButton homeBtn = new JButton("🏠 Home");
            homeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            homeBtn.setForeground(BG_PRIMARY);
            homeBtn.setBackground(PRIMARY);
            homeBtn.setBorder(null);
            homeBtn.setFocusPainted(false);
            homeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            homeBtn.addActionListener(e -> cards.show(cardPanel, "dashboard"));
            left.add(homeBtn);
        } else {
            JLabel logo = new JLabel("🏨 Luxe Hotels");
            logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
            logo.setForeground(BG_PRIMARY);
            left.add(logo);
        }

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.setOpaque(false);

        JLabel userLabel = new JLabel(currentUser != null ? "👤 " + currentUser.getUsername() : "Guest");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(BG_PRIMARY);
        right.add(userLabel);

        JButton logoutBtn = createSmallButton("Logout");
        logoutBtn.setForeground(BG_PRIMARY);
        logoutBtn.setBackground(new Color(180, 30, 60));
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            selectionMap.clear();
            cart.clear();
            cards.show(cardPanel, "auth");
        });
        right.add(logoutBtn);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createWelcomeCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel welcome = new JLabel("Welcome back, " + (currentUser != null ? currentUser.getUsername() : "Guest") + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(DARK_TEXT);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Discover luxury, comfort, and unforgettable experiences");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(LIGHT_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(welcome);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(subtitle);

        card.add(textPanel, BorderLayout.WEST);
        return card;
    }

    private JPanel createActionCard(String emoji, String title, Runnable action) {
        JPanel card = createStyledCard();
        card.setLayout(new GridBagLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (action != null) action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(15, new Color(41, 128, 185), 2),
                        new EmptyBorder(15, 15, 15, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new RoundedBorder(15, BG_TERTIARY, 1));
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        gbc.gridy = 0;
        card.add(emojiLabel, gbc);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_TEXT);
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 10, 10, 10);
        card.add(titleLabel, gbc);

        return card;
    }

    // ============ ROOMS PANEL ============
    private JPanel createRoomsPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_SECONDARY);
        root.add(createHeader(), BorderLayout.NORTH);

        // Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setBackground(BG_PRIMARY);
        filterPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        String[] roomTypes = {"All Rooms", "Standard", "Deluxe", "Suite"};
        JComboBox<String> typeFilter = new JComboBox<>(roomTypes);
        typeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterPanel.add(new JLabel("Room Type:"));
        filterPanel.add(typeFilter);

        root.add(filterPanel, BorderLayout.SOUTH);

        // Room grid
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(BG_SECONDARY);
        gridPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        for (Room room : rooms) {
            gridPanel.add(createRoomCard(room));
        }

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBackground(BG_SECONDARY);
        root.add(scroll, BorderLayout.CENTER);

        // Floating cart button
        addFloatingCartButton(root);

        return root;
    }

    private JPanel createRoomCard(Room room) {
        JPanel card = createStyledCard();
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Room number
        JLabel roomNum = new JLabel("Room #" + room.getRoomNumber());
        roomNum.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomNum.setForeground(PRIMARY);
        gbc.gridy = 0;
        card.add(roomNum, gbc);

        // Type
        JLabel type = new JLabel(room.getRoomType());
        type.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        type.setForeground(DARK_TEXT);
        gbc.gridy = 1;
        card.add(type, gbc);

        // Price
        JLabel price = new JLabel("Rs. " + room.getPricePerNight() + "/night");
        price.setFont(new Font("Segoe UI", Font.BOLD, 16));
        price.setForeground(SECONDARY);
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 15, 0);
        card.add(price, gbc);

        // Availability status
        String status = room.isAvailable() ? "Available" : "Booked";
        Color statusColor = room.isAvailable() ? SECONDARY : ERROR;
        JLabel statusLabel = new JLabel("● " + status);
        statusLabel.setForeground(statusColor);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 0, 15, 0);
        card.add(statusLabel, gbc);

        // Book button
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        JButton bookBtn = createModernButton("Book Now", room.isAvailable() ? PRIMARY : new Color(189, 195, 199));
        bookBtn.setEnabled(room.isAvailable() && currentUser != null);
        
        Runnable bookAction = () -> {
            if (currentUser == null) {
                showErrorDialog("Login Required", "Please login to book a room.");
                return;
            }
            if (bookingService.bookRoom(room.getRoomNumber())) {
                cart.addRoom(room);
                showSuccessDialog("Room Booked", "Room " + room.getRoomNumber() + " added to cart!");
                refreshCartDisplay();
            } else {
                showErrorDialog("Booking Failed", "This room is no longer available.");
            }
        };
        
        // Add action listener
        bookBtn.addActionListener(e -> bookAction.run());
        
        // Add mouse listener as backup for event propagation issues
        bookBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (bookBtn.isEnabled()) {
                    bookAction.run();
                }
            }
        });
        
        // Add card-level click handler
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (room.isAvailable() && currentUser != null) {
                    bookAction.run();
                }
            }
        });

        card.add(bookBtn, gbc);
        return card;
    }

    // ============ FOOD PANEL ============
    private JPanel createFoodPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_SECONDARY);
        root.add(createHeader(), BorderLayout.NORTH);

        // Group by cuisine
        Map<String, List<TakeawayItem>> cuisineMap = new TreeMap<>();
        for (TakeawayItem item : takeawayMenu) {
            cuisineMap.computeIfAbsent(item.getCuisine(), k -> new ArrayList<>()).add(item);
        }

        // Use BoxLayout for vertical stacking to prevent collapse
        JPanel foodContent = new JPanel();
        foodContent.setLayout(new BoxLayout(foodContent, BoxLayout.Y_AXIS));
        foodContent.setBackground(BG_SECONDARY);
        foodContent.setBorder(new EmptyBorder(20, 30, 20, 30));

        for (Map.Entry<String, List<TakeawayItem>> entry : cuisineMap.entrySet()) {
            // Cuisine label
            JLabel cuisineLabel = new JLabel(entry.getKey() + " Cuisine");
            cuisineLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            cuisineLabel.setForeground(PRIMARY);
            cuisineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            cuisineLabel.setBorder(new EmptyBorder(20, 0, 15, 0));
            foodContent.add(cuisineLabel);

            // Items grid in wrapper to control layout
            JPanel itemsWrapper = new JPanel();
            itemsWrapper.setLayout(new GridLayout(0, 3, 20, 20));
            itemsWrapper.setBackground(BG_SECONDARY);
            itemsWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            itemsWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (TakeawayItem item : entry.getValue()) {
                itemsWrapper.add(createFoodCard(item));
            }
            foodContent.add(itemsWrapper);
            
            // Add spacing between sections
            JPanel spacer = new JPanel();
            spacer.setOpaque(false);
            spacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
            foodContent.add(spacer);
        }

        // Add vertical glue to push content to top
        foodContent.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(foodContent);
        scroll.setBackground(BG_SECONDARY);
        scroll.getViewport().setBackground(BG_SECONDARY);
        root.add(scroll, BorderLayout.CENTER);
        
        // Floating cart button
        addFloatingCartButton(root);
        return root;
    }

    private JPanel createFoodCard(TakeawayItem item) {
        JPanel card = createStyledCard();
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Dish name
        JLabel name = new JLabel(item.getName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(DARK_TEXT);
        gbc.gridy = 0;
        card.add(name, gbc);

        // Cuisine
        JLabel cuisine = new JLabel(item.getCuisine());
        cuisine.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cuisine.setForeground(LIGHT_TEXT);
        gbc.gridy = 1;
        card.add(cuisine, gbc);

        // Price
        JLabel price = new JLabel("Rs. " + item.getPrice());
        price.setFont(new Font("Segoe UI", Font.BOLD, 14));
        price.setForeground(SECONDARY);
        gbc.gridy = 2;
        gbc.insets = new Insets(12, 0, 12, 0);
        card.add(price, gbc);

        // Quantity controls
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        qtyPanel.setOpaque(false);

        JButton minus = new JButton("−");
        minus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        minus.setPreferredSize(new Dimension(30, 30));
        minus.setBackground(BG_TERTIARY);
        minus.setBorder(new RoundedBorder(6, BG_TERTIARY, 1));
        minus.setFocusPainted(false);

        JLabel qtyLabel = new JLabel("0");
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qtyLabel.setForeground(DARK_TEXT);
        qtyLabel.setPreferredSize(new Dimension(30, 30));
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton plus = new JButton("+");
        plus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        plus.setPreferredSize(new Dimension(30, 30));
        plus.setBackground(SECONDARY);
        plus.setForeground(BG_PRIMARY);
        plus.setBorder(new RoundedBorder(6, SECONDARY, 1));
        plus.setFocusPainted(false);
        plus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        minus.addActionListener(e -> {
            if (currentUser == null) return;
            int q = selectionMap.getOrDefault(item, 0);
            if (q > 0) {
                q--;
                selectionMap.put(item, q);
                qtyLabel.setText(String.valueOf(q));
                cart.removeTakeawayItem(item);
            }
        });

        plus.addActionListener(e -> {
            if (currentUser == null) {
                showErrorDialog("Login Required", "Please login to order food.");
                return;
            }
            int q = selectionMap.getOrDefault(item, 0);
            if (q < 10) {
                q++;
                selectionMap.put(item, q);
                qtyLabel.setText(String.valueOf(q));
                cart.addTakeawayItem(item);
            }
        });

        qtyPanel.add(minus);
        qtyPanel.add(qtyLabel);
        qtyPanel.add(plus);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(qtyPanel, gbc);

        return card;
    }

    // Refresh cart panel before showing it
    private void refreshCartDisplay() {
        if (cartPanel != null) {
            cardPanel.remove(cartPanel);
        }
        cartPanel = createCartPanel();
        cardPanel.add(cartPanel, "cart");
    }

    // ============ CART PANEL ============
    private JPanel createCartPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_SECONDARY);
        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_SECONDARY);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        // Cart items
        JPanel itemsCard = createStyledCard();
        itemsCard.setLayout(new GridBagLayout());
        itemsCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints igbc = new GridBagConstraints();
        igbc.fill = GridBagConstraints.HORIZONTAL;
        igbc.weightx = 1;

        JLabel cartTitle = new JLabel("Your Cart");
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cartTitle.setForeground(PRIMARY);
        igbc.gridy = 0;
        igbc.insets = new Insets(0, 0, 20, 0);
        itemsCard.add(cartTitle, igbc);

        // Rooms
        if (!cart.getBookedRooms().isEmpty()) {
            JLabel roomsLabel = new JLabel("Rooms");
            roomsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            roomsLabel.setForeground(DARK_TEXT);
            igbc.gridy++;
            igbc.insets = new Insets(10, 0, 10, 0);
            itemsCard.add(roomsLabel, igbc);

            for (Room r : cart.getBookedRooms()) {
                JLabel roomItem = new JLabel("Room #" + r.getRoomNumber() + " (" + r.getRoomType() + ") - Rs. " + r.getPricePerNight());
                roomItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                roomItem.setForeground(LIGHT_TEXT);
                igbc.gridy++;
                igbc.insets = new Insets(5, 15, 5, 0);
                itemsCard.add(roomItem, igbc);
            }
        }

        // Food items (already grouped by cart as Map<Item, Qty>)
        if (!cart.getTakeawayItems().isEmpty()) {
            JLabel foodLabel = new JLabel("Food Items");
            foodLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            foodLabel.setForeground(DARK_TEXT);
            igbc.gridy++;
            igbc.insets = new Insets(20, 0, 10, 0);
            itemsCard.add(foodLabel, igbc);

            for (Map.Entry<TakeawayItem, Integer> entry : cart.getTakeawayItems().entrySet()) {
                JLabel foodItem = new JLabel(entry.getValue() + "x " + entry.getKey().getName());
                foodItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                foodItem.setForeground(LIGHT_TEXT);
                igbc.gridy++;
                igbc.insets = new Insets(5, 15, 5, 0);
                itemsCard.add(foodItem, igbc);
            }
        }

        if (cart.getBookedRooms().isEmpty() && cart.getTakeawayItems().isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            emptyLabel.setForeground(LIGHT_TEXT);
            igbc.gridy++;
            itemsCard.add(emptyLabel, igbc);
        }

        igbc.gridy++;
        igbc.weighty = 1;
        itemsCard.add(new JLabel(), igbc);

        gbc.gridy = 0;
        gbc.weighty = 0.6;
        content.add(itemsCard, gbc);

        // Summary card
        JPanel summaryCard = createStyledCard();
        summaryCard.setLayout(new GridBagLayout());
        summaryCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        summaryCard.setBackground(new Color(245, 250, 255));

        GridBagConstraints sgbc = new GridBagConstraints();
        sgbc.fill = GridBagConstraints.HORIZONTAL;
        sgbc.weightx = 1;

        JLabel summaryTitle = new JLabel("Order Summary");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setForeground(PRIMARY);
        sgbc.gridy = 0;
        sgbc.insets = new Insets(0, 0, 15, 0);
        summaryCard.add(summaryTitle, sgbc);

        JLabel totalLabel = new JLabel("Total: Rs. " + cart.getTotalBill());
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(SECONDARY);
        sgbc.gridy = 1;
        sgbc.insets = new Insets(0, 0, 20, 0);
        summaryCard.add(totalLabel, sgbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton checkoutBtn = createModernButton("Checkout", SECONDARY);
        checkoutBtn.setPreferredSize(new Dimension(150, 40));
        checkoutBtn.addActionListener(e -> {
            if (cart.getBookedRooms().isEmpty() && cart.getTakeawayItems().isEmpty()) {
                showErrorDialog("Empty Cart", "Please add items before checkout.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(mainFrame,
                    "Total: Rs. " + cart.getTotalBill() + "\nProceed to payment?",
                    "Confirm Checkout", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                showSuccessDialog("Payment Complete", "Thank you for your order!");
                cart.clear();
                selectionMap.clear();
                cards.show(cardPanel, "dashboard");
            }
        });
        buttonPanel.add(checkoutBtn);

        sgbc.gridy = 2;
        summaryCard.add(buttonPanel, sgbc);

        gbc.gridy = 1;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(20, 0, 0, 0);
        content.add(summaryCard, gbc);

        root.add(content, BorderLayout.CENTER);
        return root;
    }

    // ============ UI HELPERS ============
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(DARK_TEXT);
        return label;
    }

    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(LIGHT_TEXT);
        field.setCaretColor(PRIMARY);
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setMinimumSize(new Dimension(200, 44));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, new Color(220, 225, 230), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(BG_PRIMARY);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DARK_TEXT);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(LIGHT_TEXT);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JPasswordField createModernPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setCaretColor(PRIMARY);
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setMinimumSize(new Dimension(200, 44));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, new Color(220, 225, 230), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(BG_PRIMARY);

        // placeholder behavior for password field
        field.setText(placeholder);
        field.setForeground(LIGHT_TEXT);
        field.setEchoChar((char)0);
        
        // Use a flag to track placeholder state
        boolean[] isPlaceholder = {true};
        
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder[0]) {
                    field.setText("");
                    field.setForeground(DARK_TEXT);
                    field.setEchoChar('•');
                    isPlaceholder[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(LIGHT_TEXT);
                    field.setText(placeholder);
                    field.setEchoChar((char)0);
                    isPlaceholder[0] = true;
                }
            }
        });

        return field;
    }

    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BG_PRIMARY);
        btn.setBackground(color);
        btn.setBorder(new RoundedBorder(8, color, 0));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));

        btn.addMouseListener(new MouseAdapter() {
            private Color originalColor = color;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // Darken color by 15-20%
                Color darkened = new Color(
                        Math.max(0, color.getRed() - 25),
                        Math.max(0, color.getGreen() - 25),
                        Math.max(0, color.getBlue() - 25)
                );
                btn.setBackground(darkened);
                btn.setBorder(new RoundedBorder(8, darkened, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
                btn.setBorder(new RoundedBorder(8, color, 0));
            }
        });

        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(PRIMARY);
        btn.setBackground(BG_PRIMARY);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(6, PRIMARY, 1),
                new EmptyBorder(6, 12, 6, 12)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createStyledCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_PRIMARY);
        card.setBorder(new RoundedBorder(15, BG_TERTIARY, 1));
        return card;
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String title, String message) {
        JOptionPane.showMessageDialog(mainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Create a styled checkbox with custom appearance
    private JCheckBox createStyledCheckBox() {
        JCheckBox cb = new JCheckBox() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = 20;
                int height = 20;
                int x = 0;
                int y = (getHeight() - height) / 2;
                
                // Draw checkbox border
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(x, y, width, height, 4, 4);
                
                // Draw fill if checked
                if (this.isSelected()) {
                    g2.setColor(PRIMARY);
                    g2.fillRoundRect(x+1, y+1, width-2, height-2, 3, 3);
                    
                    // Draw checkmark
                    g2.setColor(BG_PRIMARY);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawLine(x+5, y+10, x+8, y+13);
                    g2.drawLine(x+8, y+13, x+15, y+6);
                }
            }
        };
        
        cb.setBackground(BG_PRIMARY);
        cb.setForeground(DARK_TEXT);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(new EmptyBorder(8, 0, 8, 0));
        cb.setFocusPainted(false);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return cb;
    }

    // Add floating cart button to a panel (rooms or food)
    private void addFloatingCartButton(JPanel panel) {
        floatingCartBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        floatingCartBtn.setForeground(BG_PRIMARY);
        floatingCartBtn.setBackground(SECONDARY);
        floatingCartBtn.setBorder(new RoundedBorder(8, SECONDARY, 0));
        floatingCartBtn.setFocusPainted(false);
        floatingCartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        floatingCartBtn.addActionListener(e -> {
            if (cart.getBookedRooms().isEmpty() && cart.getTakeawayItems().isEmpty()) {
                showErrorDialog("Empty Cart", "Please add items before proceeding.");
                return;
            }
            refreshCartDisplay();
            cards.show(cardPanel, "cart");
        });

        // Simply add button to the panel - it will appear at the bottom
        // Use a JPanel wrapper with FlowLayout to position it
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.setBackground(BG_SECONDARY);
        buttonWrapper.setBorder(new EmptyBorder(10, 10, 10, 20));
        buttonWrapper.add(floatingCartBtn);
        
        if (panel.getLayout() instanceof BorderLayout) {
            panel.add(buttonWrapper, BorderLayout.SOUTH);
        } else {
            panel.add(floatingCartBtn);
        }
    }

    // Wrap a password field with an embedded eye icon to toggle visibility
    private JPanel wrapPasswordFieldWithEye(JPasswordField field) {
        JPanel wrapper = new JPanel(new BorderLayout(5, 0));
        wrapper.setBackground(BG_PRIMARY);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, new Color(220, 225, 230), 1),
                new EmptyBorder(8, 12, 8, 8)
        ));

        field.setBackground(BG_PRIMARY);
        field.setBorder(null);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setCaretColor(PRIMARY);
        field.setEchoChar('•');

        JButton eyeBtn = new JButton("\uD83D\uDC41"); // Eye emoji with proper Unicode
        eyeBtn.setFocusPainted(false);
        eyeBtn.setBackground(BG_PRIMARY);
        eyeBtn.setForeground(PRIMARY);
        eyeBtn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(4, new Color(220, 225, 230), 1),
                new EmptyBorder(2, 4, 2, 4)
        ));
        eyeBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        eyeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeBtn.addActionListener(e -> {
            if (field.getEchoChar() == 0) {
                field.setEchoChar('•');
            } else {
                field.setEchoChar((char)0);
            }
        });

        wrapper.add(field, BorderLayout.CENTER);
        wrapper.add(eyeBtn, BorderLayout.EAST);
        return wrapper;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        // Stricter email validation: must have valid structure
        // Standard email regex that rejects s@s, test@s, etc.
        Pattern strictEmailPattern = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]{2,}\\.[A-Za-z]{2,}$"
        );
        return strictEmailPattern.matcher(email).matches();
    }

    // Show a simple Terms & Conditions modal. If the user agrees, the provided checkbox will be selected.
    private void showTermsWindow(JCheckBox termsBox) {
        JDialog dlg = new JDialog(mainFrame, "Terms & Conditions", true);
        dlg.setSize(600, 400);
        dlg.setLocationRelativeTo(mainFrame);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText("Terms and Conditions\n\nPlease read these terms carefully. By agreeing you accept our rules and privacy policy.\n\n[Placeholder terms text]\n\nThis is a demo application; replace this text with your real Terms & Conditions.");

        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(BG_PRIMARY);
        JButton agree = new JButton("I Agree");
        agree.addActionListener(e -> {
            termsBox.setSelected(true);
            dlg.dispose();
        });
        JButton close = new JButton("Close");
        close.addActionListener(e -> dlg.dispose());
        bottom.add(close);
        bottom.add(agree);

        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(sp, BorderLayout.CENTER);
        dlg.getContentPane().add(bottom, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // Show account information dialog
    private void showAccountDialog() {
        JDialog dlg = new JDialog(mainFrame, "Account Information", true);
        dlg.setSize(500, 400);
        dlg.setLocationRelativeTo(mainFrame);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_PRIMARY);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Account Details");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY);
        gbc.gridy = 0;
        content.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username: " + (currentUser != null ? currentUser.getUsername() : "N/A"));
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(DARK_TEXT);
        gbc.gridy = 1;
        content.add(usernameLabel, gbc);

        JLabel memberLabel = new JLabel("Member Since: 2025");
        memberLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberLabel.setForeground(LIGHT_TEXT);
        gbc.gridy = 2;
        content.add(memberLabel, gbc);

        JLabel statusLabel = new JLabel("Status: Active");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(SECONDARY);
        gbc.gridy = 3;
        content.add(statusLabel, gbc);

        gbc.gridy = 4;
        gbc.weighty = 1;
        content.add(new JLabel(), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dlg.dispose());
        buttonPanel.add(closeBtn);

        dlg.getContentPane().setLayout(new BorderLayout());
        dlg.getContentPane().add(content, BorderLayout.CENTER);
        dlg.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ============ ROUNDED BORDER ============
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final int thickness;

        RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }
}
