package umn.ac.id.project.maggot.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import umn.ac.id.project.maggot.global.Helper;

public class UserModel {

    private List<User> users;
    private User user;

    public List<User> getUsers() {
        return users;
    }

    public User getUser() {
        return user;
    }

    public class User {
        private int id;
        private String full_name;
        private String username;
        private String email;
        private String address;
        private double balance;
        private String role;
        private Date created_at;
        private Date updated_at;
        private int is_verified;

        public int is_verified() {
            return is_verified;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getAddress() {
            return address;
        }

        public String getUsername() {
            return username;
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

        public Date getCreated_at_date_time() {
            return this.created_at;
        }

        public Date getUpdated_at_date_time() {
            return this.updated_at;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", full_name='" + full_name + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", balance=" + balance +
                    ", role='" + role + '\'' +
                    ", created_at=" + created_at +
                    ", updated_at=" + updated_at +
                    ", is_verified=" + is_verified +
                    '}';
        }
    }
}
