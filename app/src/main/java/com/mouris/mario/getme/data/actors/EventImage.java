package com.mouris.mario.getme.data.actors;

public class EventImage extends Actor {
    public static final String ROOT_REF_NAME = "EventImages";

    public String owner, statement, imageUrl;

    public EventImage(String owner, String statement, String imageUrl) {
        this.owner = owner;
        this.statement = statement;
        this.imageUrl = imageUrl;
    }
}
