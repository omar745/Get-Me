package com.mouris.mario.getme.data.actors;

public class EventImage extends Actor {
    public static final String ROOT_REF_NAME = "EventImages";

    public String owner, caption, imageUrl;

    public EventImage(String owner, String caption, String imageUrl) {
        this.owner = owner;
        this.caption = caption;
        this.imageUrl = imageUrl;
    }
}
