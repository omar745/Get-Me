package com.mouris.mario.getme.utils;

import com.mouris.mario.getme.data.actors.Actor;
import com.mouris.mario.getme.data.actors.Gift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListUtils {
    private ListUtils() { }

    public static <T extends Actor> T searchListById(List<T> listOfItems, String id) {
        if (listOfItems == null || id == null) return null;
        for (T item : listOfItems) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    static public List<Gift> getGiftsListFromMap(HashMap<String, Gift> giftsMap) {
        List<Gift> giftsList = new ArrayList<>();

        for (String giftKey : giftsMap.keySet()) {
            Gift gift = giftsMap.get(giftKey);
            gift.id = giftKey;

            giftsList.add(gift);
        }

        return giftsList;
    }
}
