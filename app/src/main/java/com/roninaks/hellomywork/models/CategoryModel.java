package com.roninaks.hellomywork.models;

public class CategoryModel {
    private int Id;
    private String Name;
    private String Link;
    private String UnionName;
    private String Icon;
    private int Frequency;
    private String Color;
    private String Tag;

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

    public String getUnionName() {
        return UnionName;
    }

    public String getIcon() {
        return Icon;
    }

    public int getFrequency() {
        return Frequency;
    }

    public String getColor() {
        return Color;
    }

    public String getTag() {
        return Tag;
    }

    //Setters

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setUnionName(String unionName) {
        UnionName = unionName;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
