package com.roninaks.hellomywork.models;

public class TopPerformerModel {
    private String userName;
    private String userConversions;
    private String id;

    public String getUsesrName() {
        return userName;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserConversions() {
        return userConversions;
    }

    public void setUserConversions(String userConversions) {
        this.userConversions = userConversions;
    }
}
