package com.roninaks.hellomywork.models;

public class AnnouncementsModel {

    private String announcementDate;
    private String announcementMessage;
    private String getAnnouncementTime;

    public String getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(String announcementDate) {
        this.announcementDate = announcementDate;
    }

    public String getAnnouncementMessage() {
        return announcementMessage;
    }

    public void setAnnouncementMessage(String announcementMessage) {
        this.announcementMessage = announcementMessage;
    }

    public String getGetAnnouncementTime() {
        return getAnnouncementTime;
    }

    public void setGetAnnouncementTime(String getAnnouncementTime) {
        this.getAnnouncementTime = getAnnouncementTime;
    }
}
