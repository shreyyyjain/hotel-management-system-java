package com.shrey.hotel.model;

import com.shrey.hotel.util.HashUtil;

import java.util.Objects;

public class User {
    private final String username;
    private String passwordHash;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean authenticate(String passwordPlain) {
        return HashUtil.checkPassword(passwordPlain, passwordHash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() { return Objects.hash(username); }
}
