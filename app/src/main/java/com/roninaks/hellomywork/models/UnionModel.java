package com.roninaks.hellomywork.models;

public class UnionModel {
    private int Id;
    private String Name;
    private String Link;

    //Getters

    public String getName() {
        return Name;
    }

    public int getId() {
        return Id;
    }

    public String getLink() {
        return Link;
    }
    //Setter


    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLink(String link) {
        Link = link;
    }
}
