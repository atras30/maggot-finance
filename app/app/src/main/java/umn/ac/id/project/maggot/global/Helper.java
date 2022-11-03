package umn.ac.id.project.maggot.global;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String parseDate(Date time) {
        String pattern = "dd MMMM yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(time);

        return date;
    }
}
