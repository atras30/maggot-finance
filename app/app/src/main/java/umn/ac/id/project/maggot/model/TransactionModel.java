package umn.ac.id.project.maggot.model;

import java.util.Date;

import umn.ac.id.project.maggot.global.Helper;

public class TransactionModel {
    private String type, description;
    private Date created_at;
    private double amount;

    public TransactionModel(String type, String description, Date created_at, double amount) {
        this.type = type;
        this.description = description;
        this.created_at = created_at;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return Helper.parseDate(created_at);
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", amount=" + amount +
                '}';
    }
}
