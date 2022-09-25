package umn.ac.id.maggotproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserModel {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public class User {
        private int id;
        private String full_name;
        private String username;
        private String email;
        private double balance;
        private String role;
        private Date created_at;
        private Date updated_at;

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
            return "Result{" +
                    "id=" + id +
                    ", full_name='" + full_name + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", balance=" + balance +
                    ", role='" + role + '\'' +
                    ", created_at='" + getCreated_at() + '\'' +
                    ", updated_at='" + getUpdated_at() + '\'' +
                    '}';
        }
    }
}
