package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class WishList extends Actor {
    public static final String ROOT_REF_NAME = "WishLists";

    public String event, owner;
    public boolean is_active;
    public HashMap<String, Gift> gifts_list;

    public WishList() {
        gifts_list = new HashMap<>();
    }

    public WishList(String event, String owner, boolean is_active, HashMap<String, Gift> gifts_list) {
        this.event = event;
        this.owner = owner;
        this.is_active = is_active;
        this.gifts_list = gifts_list;
    }
}
