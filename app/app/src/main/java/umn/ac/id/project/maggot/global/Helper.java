package umn.ac.id.project.maggot.global;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;

import umn.ac.id.project.maggot.FarmerDashboardActivity;
import umn.ac.id.project.maggot.FarmerTransactionActivity;
import umn.ac.id.project.maggot.R;

public class Helper {
    public static String parseDate(Date time) {
        String pattern = "dd MMMM yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(time);

        return date;
    }
}
