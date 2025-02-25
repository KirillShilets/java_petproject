package org.myproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.myproject.entity.enums.Color;

@Entity
public class Socks {
    @Id
    private Long id;
    private Color color;
    private int cottonPart;
    private int quantity;

    public Socks() {}

    public Socks(Color color, int cottonPart, int quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getCottonPart() {
        return cottonPart;
    }

    public void setCottonPart(int cottonPart) {
        this.cottonPart = cottonPart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
