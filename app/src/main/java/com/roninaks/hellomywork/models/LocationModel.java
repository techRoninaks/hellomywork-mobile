package com.roninaks.hellomywork.models;

public class LocationModel {
    private String Name;
    private int Id;

    //Getters
    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    //Setters
    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }
}
