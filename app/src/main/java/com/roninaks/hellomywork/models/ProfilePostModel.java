package com.roninaks.hellomywork.models;

import java.util.ArrayList;

public class ProfilePostModel {

    private String name;
    private String description;
    private String location;
    private String date;
    private String time;
    private String imageUri;
    private String id;
    private String likeCount;
    private String commentCount;
    private String isBoomarked ;
    private String isLiked ;
    private String imageLabel;
    private boolean commentLoaded;


    public ProfilePostModel(){
        commentLoaded = false;
    }


    public boolean isCommentLoaded() {
        return commentLoaded;
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getIsBoomarked() {
        return isBoomarked;
    }

    public void setIsBoomarked(String isBoomarked) {
        this.isBoomarked = isBoomarked;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    private ArrayList<CommentsModel> commentsModels;

    public ArrayList<CommentsModel> getCommentsModels() {
        return commentsModels;
    }

    public void setCommentsModels(ArrayList<CommentsModel> commentsModels) {
        this.commentsModels = commentsModels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCommentLoaded(boolean commentLoaded){this.commentLoaded = commentLoaded;}
}
