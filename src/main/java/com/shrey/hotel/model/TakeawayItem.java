package com.shrey.hotel.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TakeawayItem {
    private final String name;
    private final String cuisine;
    private final BigDecimal price;

    public TakeawayItem(String name, String cuisine, BigDecimal price) {
        this.name = name;
        this.cuisine = cuisine;
        this.price = price;
    }

    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public BigDecimal getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TakeawayItem)) return false;
        TakeawayItem that = (TakeawayItem) o;
        return Objects.equals(name, that.name) && Objects.equals(cuisine, that.cuisine);
    }

    @Override
    public int hashCode() { return Objects.hash(name, cuisine); }
}
