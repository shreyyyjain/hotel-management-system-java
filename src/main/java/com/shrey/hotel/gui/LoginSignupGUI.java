package com.shrey.hotel.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.shrey.hotel.model.Cart;
import com.shrey.hotel.model.Room;
import com.shrey.hotel.model.TakeawayItem;
import com.shrey.hotel.model.User;
import com.shrey.hotel.service.BookingService;
import com.shrey.hotel.util.HashUtil;

/**
 * Modern flat light UI for the Hotel app.
 * Replace the old LoginSignupGUI with this file.
 */
public class LoginSignupGUI {
    private final List<User> users;
    private User currentUser;
    private final List<Room> rooms;
    private final List<TakeawayItem> takeawayMenu;
    private final Cart cart;
    private final BookingService bookingService;

    private JFrame mainFrame;
    private JFrame cartFrame;

    public LoginSignupGUI(List<User> users, Cart cart, List<Room> rooms, List<TakeawayItem> takeawayMenu, BookingService bookingService) {
        this.users = users;
        this.cart = cart;
        this.rooms = rooms;
        this.takeawayMenu = takeawayMenu;
        this.bookingService = bookingService;
    }

    public void show() {
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        SwingUtilities.invokeLater(this::createAndShowMain);
    }

    private void createAndShowMain() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Hotel Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new Dimension(900, 600));
        mainFrame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel title = new JLabel("Hotel Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(34, 47, 62));
        header.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Not signed in");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(new Color(100, 110, 120));
        header.add(userLabel, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // Main panels
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(245, 247, 250));
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Left card (actions)
        JPanel leftCard = createCard();
        leftCard.setLayout(new BoxLayout(leftCard, BoxLayout.Y_AXIS));
        JLabel actionsLabel = sectionTitle("Actions");
        leftCard.add(actionsLabel);
        leftCard.add(Box.createRigidArea(new Dimension(0, 10)));

        leftCard.add(styledButton("Login", e -> {
            showLoginDialog(userLabel);
        }));
        leftCard.add(Box.createRigidArea(new Dimension(0, 8)));
        leftCard.add(styledButton("Signup", e -> showSignupDialog(userLabel)));
        leftCard.add(Box.createRigidArea(new Dimension(0, 8)));
        leftCard.add(styledButton("Book a Room", e -> showBookingDialog()));
        leftCard.add(Box.createRigidArea(new Dimension(0, 8)));
        leftCard.add(styledButton("Order Takeaway", e -> showTakeawayDialog()));
        leftCard.add(Box.createVerticalGlue());

        // Right card (summary + cart)
        JPanel rightCard = createCard();
        rightCard.setLayout(new BorderLayout(12, 12));
        rightCard.add(sectionTitle("Summary"), BorderLayout.NORTH);

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setOpaque(false);
        summary.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        updateSummaryText(summary);

        JScrollPane summaryScroll = new JScrollPane(summary);
        summaryScroll.setBorder(null);
        summaryScroll.setOpaque(false);
        summaryScroll.getViewport().setOpaque(false);
        rightCard.add(summaryScroll, BorderLayout.CENTER);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        bottomRow.setOpaque(false);
        JButton viewCartBtn = styledButton("View Cart", e -> showCartDialog());
        bottomRow.add(viewCartBtn);
        rightCard.add(bottomRow, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.33;
        body.add(leftCard, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.67;
        body.add(rightCard, gbc);

        root.add(body, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 10, 18, 10));
        JLabel foot = new JLabel("Demo • Java Swing • Clean UI");
        foot.setForeground(new Color(140, 150, 160));
        footer.add(foot);
        root.add(footer, BorderLayout.SOUTH);

        mainFrame.setContentPane(root);
        mainFrame.setVisible(true);
    }

    // ---------- UI Helpers ----------
    private JPanel createCard() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 233, 238), 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(new Color(34, 47, 62));
        return l;
    }

    private JButton styledButton(String text, java.util.function.Consumer<ActionEvent> onClick) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(8, new Color(33, 150, 243)));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addActionListener(onClick::accept);
        return btn;
    }

    // ---------- Dialogs ----------
    private void showLoginDialog(JLabel userLabel) {
        JDialog dlg = dialog("Login");
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        gbc.gridy = 0; gbc.gridx = 0; content.add(new JLabel("Username"), gbc);
        gbc.gridy = 1; gbc.gridx = 0; content.add(userField, gbc);
        gbc.gridy = 2; gbc.gridx = 0; content.add(new JLabel("Password"), gbc);
        gbc.gridy = 3; gbc.gridx = 0; content.add(passField, gbc);

        // eye toggle for password visibility
        JButton eye = new JButton("👁");
        eye.setFocusPainted(false);
        eye.addActionListener(ev -> {
            if (passField.getEchoChar() == (char)0) passField.setEchoChar('*'); else passField.setEchoChar((char)0);
        });
        gbc.gridy = 3; gbc.gridx = 1; content.add(eye, gbc);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        row.setOpaque(false);
        JButton loginBtn = styledButton("Login", e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword());
            Optional<User> opt = users.stream().filter(x -> x.getUsername().equals(u)).findFirst();
            if (opt.isPresent() && opt.get().authenticate(p)) {
                currentUser = opt.get();
                userLabel.setText("Signed in: " + currentUser.getUsername());
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dlg.dispose());
        row.add(cancel);
        row.add(loginBtn);

        gbc.gridy = 4; content.add(row, gbc);

        dlg.getContentPane().add(content);
        dlg.pack();
        dlg.setLocationRelativeTo(mainFrame);
        dlg.setVisible(true);
    }

    private void showSignupDialog(JLabel userLabel) {
        JDialog dlg = dialog("Signup");
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        gbc.gridy = 0; content.add(new JLabel("Choose username"), gbc);
        gbc.gridy = 1; content.add(userField, gbc);
        gbc.gridy = 2; content.add(new JLabel("Choose password"), gbc);
        gbc.gridy = 3; content.add(passField, gbc);
        gbc.gridy = 4; content.add(new JLabel("Confirm password"), gbc);
        gbc.gridy = 5; content.add(confirmField, gbc);

        // eye toggle for password fields
        JButton eye1 = new JButton("👁"); eye1.setFocusPainted(false);
        eye1.addActionListener(ev -> { if (passField.getEchoChar()==(char)0) passField.setEchoChar('*'); else passField.setEchoChar((char)0); });
        JButton eye2 = new JButton("👁"); eye2.setFocusPainted(false);
        eye2.addActionListener(ev -> { if (confirmField.getEchoChar()==(char)0) confirmField.setEchoChar('*'); else confirmField.setEchoChar((char)0); });
        gbc.gridy = 3; gbc.gridx = 1; content.add(eye1, gbc);
        gbc.gridy = 5; gbc.gridx = 1; content.add(eye2, gbc);
        gbc.gridx = 0;

        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        row.setOpaque(false);
        JButton signBtn = styledButton("Signup", e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword());
            String p2 = new String(confirmField.getPassword());
            if (u.isEmpty() || p.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!p.equals(p2)) {
                JOptionPane.showMessageDialog(dlg, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            synchronized (users) {
                boolean exists = users.stream().anyMatch(x -> x.getUsername().equals(u));
                if (exists) {
                    JOptionPane.showMessageDialog(dlg, "Username exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                users.add(new User(u, HashUtil.hashPassword(p)));
            }
            userLabel.setText("Signed in: " + u);
            currentUser = users.stream().filter(x -> x.getUsername().equals(u)).findFirst().orElse(null);
            JOptionPane.showMessageDialog(dlg, "Signed up!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose();
        });
        JButton cancel = new JButton("Cancel");
        cancel.setFocusPainted(false);
        cancel.addActionListener(e -> dlg.dispose());
        row.add(cancel);
        row.add(signBtn);

        gbc.gridy = 4; content.add(row, gbc);

        dlg.getContentPane().add(content);
        dlg.pack();
        dlg.setLocationRelativeTo(mainFrame);
        dlg.setVisible(true);
    }

    private void showBookingDialog() {
        JDialog dlg = dialog("Book Room");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        List<Room> avail = rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
        if (avail.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No rooms available", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // date selectors (simple spinners)
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateRow.setOpaque(false);
        dateRow.add(new JLabel("Check-in:"));
        JSpinner checkin = new JSpinner(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        checkin.setEditor(new JSpinner.DateEditor(checkin, "yyyy-MM-dd"));
        dateRow.add(checkin);
        dateRow.add(new JLabel("Check-out:"));
        JSpinner checkout = new JSpinner(new javax.swing.SpinnerDateModel(new java.util.Date(System.currentTimeMillis()+86400000L), null, null, java.util.Calendar.DAY_OF_MONTH));
        checkout.setEditor(new JSpinner.DateEditor(checkout, "yyyy-MM-dd"));
        dateRow.add(checkout);
        panel.add(dateRow);

        for (Room r : avail) {
            JPanel row = new JPanel(new BorderLayout(8, 8));
            row.setOpaque(false);
            JLabel lbl = new JLabel(String.format("Room %d • %s • Rs.%s", r.getRoomNumber(), r.getRoomType(), r.getPricePerNight().toPlainString()));
            JButton b = styledButton("Book", ev -> {
                if (currentUser == null) { JOptionPane.showMessageDialog(mainFrame, "Please login to book rooms.", "Login required", JOptionPane.INFORMATION_MESSAGE); return; }
                // show selected dates in confirmation
                java.util.Date in = (java.util.Date) checkin.getValue();
                java.util.Date out = (java.util.Date) checkout.getValue();
                int confirm = JOptionPane.showConfirmDialog(dlg, String.format("Book Room %d from %tF to %tF?", r.getRoomNumber(), in, out), "Confirm Booking", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                boolean ok = bookingService.bookRoom(r.getRoomNumber());
                if (ok) {
                    cart.addRoom(r);
                    JOptionPane.showMessageDialog(dlg, "Booked and added to cart", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Failed to book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            row.add(lbl, BorderLayout.CENTER);
            row.add(b, BorderLayout.EAST);
            panel.add(row);
            panel.add(Box.createRigidArea(new Dimension(0,8)));
        }
        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(null);
        dlg.getContentPane().add(sp);
        dlg.pack();
        dlg.setSize(560, Math.min(420, dlg.getHeight()));
        dlg.setLocationRelativeTo(mainFrame);
        dlg.setVisible(true);
    }

    private void showTakeawayDialog() {
        JDialog dlg = dialog("Takeaway Menu");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        Map<String, List<TakeawayItem>> map = new TreeMap<>();
        for (TakeawayItem t : takeawayMenu) map.computeIfAbsent(t.getCuisine(), k -> new ArrayList<>()).add(t);

        Map<TakeawayItem, Integer> selection = new HashMap<>();
        for (Map.Entry<String, List<TakeawayItem>> e : map.entrySet()) {
            JLabel cuisine = new JLabel(e.getKey());
            cuisine.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panel.add(cuisine);
            panel.add(Box.createRigidArea(new Dimension(0,6)));
            for (TakeawayItem item : e.getValue()) {
                JPanel row = new JPanel(new BorderLayout(8,8));
                row.setOpaque(false);
                JLabel lbl = new JLabel(item.getName() + " • Rs." + item.getPrice().toPlainString());

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                right.setOpaque(false);

                final JButton[] addRef = new JButton[1];
                JButton add = styledButton("Add", ev -> {
                    if (currentUser == null) { JOptionPane.showMessageDialog(dlg, "Please login to order.", "Login required", JOptionPane.INFORMATION_MESSAGE); return; }
                    selection.put(item, 1);
                    cart.addTakeawayItem(item);
                    // replace add with qty controls
                    right.remove(addRef[0]);
                    JButton minus = new JButton("-");
                    JLabel qtyLabel = new JLabel("1");
                    JButton plus = new JButton("+");
                    minus.setFocusPainted(false); plus.setFocusPainted(false);
                    minus.addActionListener(ae -> {
                        int q = selection.getOrDefault(item, 0);
                        if (q > 0) {
                            q--;
                            selection.put(item, q);
                            qtyLabel.setText(String.valueOf(q));
                            cart.removeTakeawayItem(item);
                        }
                        if (q == 0) {
                            selection.remove(item);
                            right.removeAll();
                            right.add(addRef[0]);
                            right.revalidate(); right.repaint();
                        }
                    });
                    plus.addActionListener(ae -> {
                        int q = selection.getOrDefault(item, 0);
                        if (q < 10) {
                            q++;
                            selection.put(item, q);
                            qtyLabel.setText(String.valueOf(q));
                            cart.addTakeawayItem(item);
                        }
                    });
                    right.add(minus); right.add(qtyLabel); right.add(plus);
                    right.revalidate(); right.repaint();
                });

                right.add(add);
                    addRef[0] = add;

                row.add(lbl, BorderLayout.CENTER);
                row.add(right, BorderLayout.EAST);
                panel.add(row);
                panel.add(Box.createRigidArea(new Dimension(0,6)));
            }
            panel.add(Box.createRigidArea(new Dimension(0,10)));
        }

        JScrollPane sp = new JScrollPane(panel);
        sp.setBorder(null);
        dlg.getContentPane().add(sp);
        dlg.pack();
        dlg.setSize(620, Math.min(480, dlg.getHeight()));
        dlg.setLocationRelativeTo(mainFrame);
        dlg.setVisible(true);
    }

    private void showCartDialog() {
        if (cartFrame != null) cartFrame.dispose();
        cartFrame = new JFrame("Cart");
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cartFrame.setSize(520, 380);
        cartFrame.setLocationRelativeTo(mainFrame);
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(new EmptyBorder(12,12,12,12));
        root.setBackground(Color.WHITE);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items.setOpaque(false);

        for (Room r : cart.getBookedRooms()) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            JLabel l = new JLabel("Room " + r.getRoomNumber() + " • Rs." + r.getPricePerNight().toPlainString());
            JButton rem = styledButton("Remove", ev -> {
                cart.removeRoom(r);
                showCartDialog();
            });
            row.add(l, BorderLayout.CENTER);
            row.add(rem, BorderLayout.EAST);
            items.add(row);
            items.add(Box.createRigidArea(new Dimension(0,8)));
        }

        for (TakeawayItem t : cart.getTakeawayItems()) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            JLabel l = new JLabel(t.getName() + " • Rs." + t.getPrice().toPlainString());
            JButton rem = styledButton("Remove", ev -> {
                cart.removeTakeawayItem(t);
                showCartDialog();
            });
            row.add(l, BorderLayout.CENTER);
            row.add(rem, BorderLayout.EAST);
            items.add(row);
            items.add(Box.createRigidArea(new Dimension(0,8)));
        }

        JScrollPane sp = new JScrollPane(items);
        sp.setBorder(null);
        root.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        JLabel total = new JLabel("Total: Rs." + cart.getTotalBill().toPlainString());
        total.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton checkout = styledButton("Checkout", e -> checkoutAction());
        bottom.add(total);
        bottom.add(checkout);
        root.add(bottom, BorderLayout.SOUTH);

        cartFrame.setContentPane(root);
        cartFrame.setVisible(true);
    }

    private void checkoutAction() {
        if (cart.getBookedRooms().isEmpty() && cart.getTakeawayItems().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Cart empty", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ans = JOptionPane.showConfirmDialog(mainFrame, "Proceed to pay Rs." + cart.getTotalBill().toPlainString() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(mainFrame, "Payment OK. Thank you!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            if (cartFrame != null) cartFrame.dispose();
            updateAllSummaries();
        }
    }

    private void updateSummaryText(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("Rooms:\n");
        for (Room r : rooms) {
            sb.append(String.format("• %d %s Rs.%s%n", r.getRoomNumber(), r.getRoomType(), r.getPricePerNight().toPlainString()));
        }
        sb.append("\nTakeaway samples:\n");
        takeawayMenu.stream().limit(8).forEach(t -> sb.append("• ").append(t.getName()).append(" (").append(t.getCuisine()).append(") Rs.").append(t.getPrice().toPlainString()).append("\n"));
        area.setText(sb.toString());
    }

    private void updateAllSummaries() {
        // refresh main summary by recreating menu (simple approach)
        if (mainFrame != null) {
            mainFrame.repaint();
        }
    }

    private JDialog dialog(String title) {
        JDialog d = new JDialog(mainFrame, title, true);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.getContentPane().setBackground(new Color(250, 250, 250));
        d.setResizable(false);
        return d;
    }

    // ---------- small rounded border helper ----------
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;
        private final Color color;
        RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }
}
