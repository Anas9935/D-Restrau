package com.example.drestrau.Objects;

public class bill_item_object {
    private String name;
    private int quantity;
    private int amount;

    public bill_item_object(String name,int qty,int amt) {
        this.name=name;
        quantity=qty;
        amount=amt;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
