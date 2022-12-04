package umn.ac.id.project.maggot.model;

import java.util.ArrayList;
import java.util.List;

public class NotificationUserModel {
    private String message;

    private ArrayList<NotificationUserModel.Notification> notifications;

    public ArrayList<NotificationUserModel.Notification> getAllNotifications() {
        return notifications;
    }

    public String rejectFarmerPurchaseRequest() {
        return message;
    }

    public String rejectFarmerWithdrawalRequest() {
        return message;
    }

    public String approveWithdrawalRequest() {
        return message;
    }

    public String approveShopWithdrawalRequest() {
        return message;
    }

    public String rejectShopWithdrawalRequest() {
        return message;
    }

    public String rejectWithdrawalRequest() {
        return message;
    }

    public String approveShopBuyRequest() {
        return message;
    }

    public String rejectShopBuyRequest() {
        return message;
    }

    public class Notification {
        private String token;
        private String type;
        private int withdrawal_amount;
        private String description;
        private double weight_in_kg;
        private double amount_per_kg;
        private int total_amount;
        private String nama_peternak;
        private String nama_pengelola_bank_sampah;

        public String getToken() {return token;}

        public String getType() {
            return type;
        }

        public int getWithdrawal_amount() {
            return withdrawal_amount;
        }

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

        public int getTotal_amount() {
            return total_amount;
        }
        @Override
        public String toString() {
            return "Notification{" +
                    "token='" + token + '\'' +
                    ", type='" + type + '\'' +
                    ", withdrawal_amount=" + withdrawal_amount +
                    ", description='" + description + '\'' +
                    ", weight_in_kg=" + weight_in_kg +
                    ", amount_per_kg=" + amount_per_kg +
                    ", nama_peternak='" + nama_peternak + '\'' +
                    ", nama_pengelola_bank_sampah='" + nama_pengelola_bank_sampah + '\'' +
                    '}';
        }
    }

    public String createFarmerWithdrawalRequest() {
        return message;
    }

    public String createShoprWithdrawalRequest() {
        return message;
    }

    public String createShopBuyRequest() {
        return message;
    }
}
