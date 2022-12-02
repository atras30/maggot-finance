package umn.ac.id.project.maggot.model;

import java.util.List;

public class NotificationUserModel {

    private List<NotificationUserModel.Notification> notifications;

    public List<NotificationUserModel.Notification> getAllNotification() {
        return notifications;
    }

    public class Notification {
        private String token;
        private String description;
        private double weight_in_kg;
        private double amount_per_kg;
        private String nama_peternak;
        private String nama_pengelola_bank_sampah;

        public String getToken() {return token;}

        public String getDescription() {return description;}

        public double getWeight_in_kg() {return weight_in_kg;}

        public double getAmount_per_kg() {
            return amount_per_kg;
        }

        public String getNama_peternak() {
            return nama_peternak;
        }

        public String getNama_pengelola_bank_sampah() {
            return nama_pengelola_bank_sampah;
        }
    }
    
}
