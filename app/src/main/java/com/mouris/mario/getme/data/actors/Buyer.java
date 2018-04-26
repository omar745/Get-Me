package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class Buyer {
    public static final String BUYER_ID = "buyer_id";
    public static final String SHARING_ENABLED = "sharing_enabled";
    public static final String NUMBER_OF_SHARERS_ALLOWED = "number_of_sharers_allowed";
    public static final String SHARER_IDS = "sharer_ids";

    public String buyer_id;
    public boolean sharing_enabled;
    public int number_of_sharers_allowed;
    public HashMap<String, Boolean> sharer_ids;

    public Buyer() {
        sharer_ids = new HashMap<>();
    }
}