package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class Wishlist extends Actor {
    public static final String ROOT_REF_NAME = "WishLists";
    public static final String GIFTS_LIST = "gifts_list";

    public String event_type, owner;
    public Long event_time;
    public boolean is_active = true;
    public HashMap<String, Gift> gifts_list;

    public Wishlist() {
        gifts_list = new HashMap<>();
    }

    public Wishlist(String event_type, String owner, boolean is_active, HashMap<String, Gift> gifts_list) {
        this.event_type = event_type;
        this.owner = owner;
        this.is_active = is_active;
        this.gifts_list = gifts_list;
    }
}
