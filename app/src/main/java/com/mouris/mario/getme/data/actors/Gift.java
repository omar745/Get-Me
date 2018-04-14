package com.mouris.mario.getme.data.actors;

public class Gift extends Actor {

    public String gift_name, description, web_link, brand;

    public Gift() { }

    public Gift(String gift_name, String web_link, String brand, String description) {
        this.gift_name = gift_name;
        this.web_link = web_link;
        this.brand = brand;
        this.description = description;
    }
}
