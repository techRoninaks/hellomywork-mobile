package com.roninaks.hellomywork.models;

public class RatingsModel {
    private String UserName;
    private int UserId;
    private int ProfileId;
    private float Rating;
    private String Review;
    private int Id;

    //Getters
    public float getRating() {
        return Rating;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public int getUserId() {
        return UserId;
    }

    public String getReview() {
        return Review;
    }

    public String getUserName() {
        return UserName;
    }

    public int getId() {
        return Id;
    }

    //Setters
    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public void setReview(String review) {
        Review = review;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setId(int id) {
        Id = id;
    }
}
