package umn.ac.id.project.maggot.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PeternakModel {
    private List<Peternak> users;

    public List<Peternak> getPeternak() {
        return users;
    }

    public class Peternak {
        private int id;
        private String full_name;
        private String username;
        private String email;
        private String address;
        private double balance;
        private String role;
        private Date created_at;
        private Date updated_at;
        private int trash_manager_id;

        public int getId() {
            return id;
        }

        public String getAddress() {
            return address;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public Peternak getSelf() { return this; }

        public double getBalance() {
            return balance;
        }

        public String getRole() {
            return role;
        }

        public int getTrash_manager_id() {
            return trash_manager_id;
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
            return "Peternak{" +
                    "id=" + id +
                    ", full_name='" + full_name + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", balance=" + balance +
                    ", role='" + role + '\'' +
                    ", created_at=" + created_at +
                    ", updated_at=" + updated_at +
                    '}';
        }
    }
}
