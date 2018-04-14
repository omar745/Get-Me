package com.mouris.mario.getme.data.actors;

public class Gift extends Actor {

    public String title, category, brand, model, description;

    public Gift() { }

    public Gift(String title, String category, String brand, String model, String description) {
        this.title = title;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.description = description;
    }
}
