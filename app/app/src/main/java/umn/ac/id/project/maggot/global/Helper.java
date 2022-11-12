package umn.ac.id.project.maggot.global;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String parseDate(Date time) {
        String pattern = "dd MMM yyyy, hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(time);

        return date;
    }
}
