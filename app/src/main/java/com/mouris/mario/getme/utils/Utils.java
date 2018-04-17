package com.mouris.mario.getme.utils;

import com.mouris.mario.getme.data.actors.Gift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Utils {

    static public String getDateStringFromMillis(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH)+1) + "/" +
                calendar.get(Calendar.YEAR);
    }

    static public List<Gift> getGiftsListFromMap(HashMap<String, Gift> giftsMap) {
        return  new ArrayList<>(giftsMap.values());
    }
}
