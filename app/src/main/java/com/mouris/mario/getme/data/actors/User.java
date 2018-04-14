package com.mouris.mario.getme.data.actors;

import java.util.HashMap;

public class User extends Actor {
    public static final String ROOT_REF_NAME = "Users";
    public static final String FRIENDS_LIST = "friends_list";

    public String display_name,
            profile_picture,
            facebook_link;

    public HashMap<String, Boolean> friends_list;

    public User() {
        friends_list = new HashMap<>();
    }

    public User(String display_name, String profile_picture, String facebook_link) {

        friends_list = new HashMap<>();

        this.display_name = display_name;
        this.profile_picture = profile_picture;
        this.facebook_link = facebook_link;
    }
}
