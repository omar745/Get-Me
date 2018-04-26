package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class Gift extends Actor {
    public static final String BUYER_INFO = "buyer_info";

    public String gift_name, description, web_link, brand;
    public HashMap<String, Buyer> buyer_info;

    public Gift() {
        buyer_info = new HashMap<>();
    }

    public Gift(String gift_name, String web_link,
                String brand, String description) {
        buyer_info = new HashMap<>();

        this.gift_name = gift_name;
        this.web_link = web_link;
        this.brand = brand;
        this.description = description;
    }

    //The gift buyer's class
    public class Buyer {
        public static final String BUYER_ID = "buyer_id";
        public static final String SHARING_ENABLED = "sharing_enabled";
        public static final String SHARER_IDS = "sharer_ids";

        public String buyer_id;
        public boolean sharing_enabled;
        public HashMap<String, Boolean> sharer_ids;

        public Buyer() {
            sharer_ids = new HashMap<>();
        }
    }
}
