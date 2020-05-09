package com.jyotishapp.jyotishi;

public class VideoCall {
    VideoCall(){}

    VideoCall(String contactUserId, String contactUserName,
              String contactInfo, long timestamp, String type){
        this.contactInfo = contactInfo;
        this.timestamp = timestamp;
        this.contactUserId = contactUserId;
        this.contactUserName = contactUserName;
        this.type = type;
    }

    private long timestamp;
    private String contactUserId;
    private String contactUserName;
    private String contactInfo;
    private String type;


    public long getTimestamp() {
        return timestamp;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getContactUserId() {
        return contactUserId;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public String getType() {
        return type;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public void setType(String type) {
        this.type = type;
    }

}
