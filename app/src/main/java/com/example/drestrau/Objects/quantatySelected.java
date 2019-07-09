package com.example.drestrau.Objects;

public class quantatySelected {
    private String foodId;
    private int quantity;
    public quantatySelected(String id,int quan){
        foodId=id;
        quantity=quan;
    }

    public String getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
