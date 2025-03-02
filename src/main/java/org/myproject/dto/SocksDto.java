package org.myproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.myproject.entity.enums.Color;

public class SocksDto {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Color color;

    @Min(value = 0, message = "Процентное значение хлопка не может быть меньше 0")
    @Max(value = 100, message = "Процентное значение хлопка не может быть больше 100")
    @NotNull
    @JsonProperty("cotton_part")
    private int cottonPart;

    @NotNull
    @Min(value = 1, message = "Количество не может быть меньше 1")
    private int quantity;

    public SocksDto() {}

    public SocksDto(Color color, int cottonPart, int quantity) {
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
}
