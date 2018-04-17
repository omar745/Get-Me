package com.mouris.mario.getme.utils;

import com.mouris.mario.getme.data.actors.Actor;

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
}