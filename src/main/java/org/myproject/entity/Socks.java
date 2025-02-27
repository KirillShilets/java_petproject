package org.myproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.myproject.entity.enums.Color;

@Entity
@Table(name = "socks")
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Color color;

    @Column(name = "cotton_part")
    @Min(0)
    @Max(100)
    @NotNull
    private int cottonPart;

    @Min(1)
    @NotNull
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
