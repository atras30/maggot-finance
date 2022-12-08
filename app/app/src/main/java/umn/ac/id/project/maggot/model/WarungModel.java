package umn.ac.id.project.maggot.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WarungModel {
    private List<Warung> users;

    public List<Warung> getWarung () {
        return users;
    }

    public class Warung {
        private int id;
        private String full_name;
        private String phone_number;
        private String email;
        private String address;
        private double balance;
        private String role;
        private int trash_manager_id;
        private Date created_at;
        private Date updated_at;

        public int getId() {
            return id;
        }

        public String getAddress() {
            return address;
        }

        public String getFull_name() {
            return full_name;
        }

        public int getTrashManagerId() {
            return trash_manager_id;
        }

        public String getEmail() {
            return email;
        }

        public double getBalance() {
            return balance;
        }

        public String getRole() {
            return role;
        }

        public String getCreated_at() {
            String pattern = "dd MMMM yyyy hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(created_at);

            return date;
        }

        public String getUpdated_at() {
            String pattern = "dd MMMM yyyy hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(updated_at);

            return date;
        }

        @Override
        public String toString() {
            return "Warung{" +
                    "id=" + id +
                    ", full_name='" + full_name + '\'' +
                    ", phone_number='" + phone_number + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", balance=" + balance +
                    ", role='" + role + '\'' +
                    ", trash_manager_id=" + trash_manager_id +
                    ", created_at=" + created_at +
                    ", updated_at=" + updated_at +
                    '}';
        }
    }
    }

