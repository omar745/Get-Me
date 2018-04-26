package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class Gift extends Actor {
    public static final String BUYER_INFO = "buyer_info";

    public String gift_name, description, web_link, brand;
    public Buyer buyer_info;

    public Gift() {
    }

    public Gift(String gift_name, String web_link,
                String brand, String description) {
        this.gift_name = gift_name;
        this.web_link = web_link;
        this.brand = brand;
        this.description = description;
    }

    //Helper methods
    public boolean isBought() {
        return buyer_info != null;
    }

    public boolean isOpenForSharing() {
        return buyer_info != null &&
                buyer_info.sharing_enabled &&
                buyer_info.sharer_ids.size() < buyer_info.number_of_sharers_allowed;
    }

    public boolean isBuyer(String userId) {
        return userId.equals(buyer_info.buyer_id);
    }

    public boolean isSharer(String userId) {
        return buyer_info.sharer_ids.containsKey(userId);
    }
}
