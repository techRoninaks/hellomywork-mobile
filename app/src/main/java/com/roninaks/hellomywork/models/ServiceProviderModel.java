package com.roninaks.hellomywork.models;

public class ServiceProviderModel {
    private int Id;
    private String OrgType;
    private String Image;
    private String Name;
    private String Role;
    private float Rating;
    private int Review;
    private String Link;
    private String Sublocation;
    private String Whatsapp;
    private String Location;
    private String Skills;
    private String UnionName;
    private String Category;
    private String Website;
    private String Phone;
    private String Email;
    private String Address;
    private String CardUrl;
    private String Password;
    private String Pincode;
    private String Phone2;
    private String State;
    private String Country;
    private boolean IsPrivate;
    private boolean IsActive;
    private boolean IsProspect;
    private boolean IsPremium;

    //Getters



    public int getReview() {
        return Review;
    }

    public String getUnionName() {
        return UnionName;
    }

    public String getOrgType() {
        return OrgType;
    }

    public String getLink() {
        return Link;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public float getRating() {
        return Rating;
    }

    public String getAddress() {
        return Address;
    }

    public String getCategory() {
        return Category;
    }

    public String getCardUrl() {
        return CardUrl;
    }

    public String getImage() {
        return Image;
    }

    public String getCountry() {
        return Country;
    }

    public String getEmail() {
        return Email;
    }

    public String getLocation() {
        return Location;
    }

    public String getPhone() {
        return Phone;
    }

    public String getRole() {
        return Role;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone2() {
        return Phone2;
    }

    public String getSkills() {
        return Skills;
    }

    public String getSublocation() {
        return Sublocation;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getWebsite() {
        return Website;
    }

    public String getWhatsapp() {
        return Whatsapp;
    }

    public String getState() {
        return State;
    }

    public boolean isPrivate(){
        return IsPrivate;
    }

    public boolean isActive(){
        return IsActive;
    }

    public boolean isProspect(){
        return IsProspect;
    }

    public boolean isPremium(){
        return IsPremium;
    }

    //Setters


    public void setUnionName(String unionName) {
        UnionName = unionName;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public void setCardUrl(String cardUrl) {
        CardUrl = cardUrl;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public void setRole(String role) {
        Role = role;
    }

    public void setSkills(String skills) {
        Skills = skills;
    }

    public void setSublocation(String sublocation) {
        Sublocation = sublocation;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public void setWhatsapp(String whatsapp) {
        Whatsapp = whatsapp;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setPrivate(boolean aPrivate) {
        IsPrivate = aPrivate;
    }

    public void setProspect(boolean prospect) {
        IsProspect = prospect;
    }

    public void setState(String state) {
        State = state;
    }

    public void setOrgType(String orgType) {
        OrgType = orgType;
    }

    public void setReview(int review) {
        Review = review;
    }

    public void setPremium(boolean premium) {
        IsPremium = premium;
    }
}
