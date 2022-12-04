package umn.ac.id.project.maggot.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import umn.ac.id.project.maggot.global.Helper;

public class TrashManagerModel {
    private List<TrashManagers> trash_managers;
    private TrashManagers user;

    public List<TrashManagers> getTrashManager() {
        return trash_managers;
    }

    public TrashManagers updateTrashManagerData() {
        return user;
    }

    public static class TrashManagers {
        private int id, super_admin_id;
        private String nama_pengelola, tempat, email, role;
        Date created_at, updated_at;
        ArrayList<UserModel.User> users;

        public ArrayList<UserModel.User> getUsers() {
            return users;
        }

        public int getId() {
            return id;
        }

        public int getSuper_admin_id() {
            return super_admin_id;
        }

        public String getNama_pengelola() {
            return nama_pengelola;
        }

        public String getTempat() {
            return tempat;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public String getCreated_at() {
            return Helper.parseDate(created_at);
        }

        public String getUpdated_at() {
            return Helper.parseDate(updated_at);
        }

        @Override
        public String toString() {
            return "TrashManagers{" +
                    "id=" + id +
                    ", super_admin_id=" + super_admin_id +
                    ", nama_pengelola='" + nama_pengelola + '\'' +
                    ", tempat='" + tempat + '\'' +
                    ", email='" + email + '\'' +
                    ", role='" + role + '\'' +
                    ", created_at=" + created_at +
                    ", updated_at=" + updated_at +
                    ", users=" + users +
                    '}';
        }
    }
}
