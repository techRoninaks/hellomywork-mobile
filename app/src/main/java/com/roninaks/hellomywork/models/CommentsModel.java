package com.roninaks.hellomywork.models;

public class CommentsModel {
    private String commentName;
    private String commentId;
    private String commentU_Id;
    private String commentP_Id;
    private String comment;
    private String commentIsReported;
    private String commentIsActive;

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentU_Id() {
        return commentU_Id;
    }

    public void setCommentU_Id(String commentU_Id) {
        this.commentU_Id = commentU_Id;
    }

    public String getCommentP_Id() {
        return commentP_Id;
    }

    public void setCommentP_Id(String commentP_Id) {
        this.commentP_Id = commentP_Id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentIsReported() {
        return commentIsReported;
    }

    public void setCommentIsReported(String commentIsReported) {
        this.commentIsReported = commentIsReported;
    }

    public String getCommentIsActive() {
        return commentIsActive;
    }

    public void setCommentIsActive(String commentIsActive) {
        this.commentIsActive = commentIsActive;
    }
}
