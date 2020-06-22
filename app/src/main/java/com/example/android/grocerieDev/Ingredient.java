package com.example.android.grocerieDev;
import java.io.Serializable;

public class Ingredient implements Serializable {
    private int id;
    private String name;
    private String amount;
    private String unit;
    private int to_buy;
    private int picked_up;
    private int category;
    private int position;

    public Ingredient() {
        super();
    }

    public Ingredient(int id, String name, String amount, String unit, int to_buy, int picked_up, int category, int position) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.to_buy = to_buy;
        this.picked_up = picked_up;
        this.category = category;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getTo_buy() {
        return to_buy;
    }

    public void setTo_buy(int to_buy) {
        this.to_buy = to_buy;
    }

    public int getPicked_up() {
        return picked_up;
    }

    public void setPicked_up(int picked_up) {
        this.picked_up = picked_up;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}