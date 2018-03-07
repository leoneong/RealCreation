package com.example.realcreation;

/**
 * Created by leone on 7/2/2018.
 */

public class Product {
    private String name;
    private int imageID;

    public  Product(int imageID){
        this.imageID = imageID;
    }

    public Product(String name, int imageID){
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }
}
