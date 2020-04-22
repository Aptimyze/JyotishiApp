package com.jyotishapp.jyotishi;

public class VoiceCall {
    VoiceCall(){

    }

    VoiceCall(String contactUserId, String contactUserName,
    String contactInfo, String timestamp, String type, long duration){
        this.contactInfo = contactInfo;
        this.timestamp = timestamp;
        this.contactUserId = contactUserId;
        this.contactUserName = contactUserName;
        this.type = type;
        this.duration = duration;
    }

    private String timestamp;
    private String contactUserId;
    private String contactUserName;
    private String contactInfo;
    private String type;
    private long duration;

    public long getDuration() {
        return duration;
    }

    public String getTimestamp() {
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

    public void setTimestamp(String timestamp) {
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

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

