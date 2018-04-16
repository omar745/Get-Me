package com.mouris.mario.getme.utils;

import java.util.Calendar;

public class Utils {

    static public String getDateStringFromMillis(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.YEAR);
    }

}
