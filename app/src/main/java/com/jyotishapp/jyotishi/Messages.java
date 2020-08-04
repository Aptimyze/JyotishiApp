package com.jyotishapp.jyotishi;

public class Messages {
    String textMessage;
    String time;
    String sender;
    String messageId;
    String imageUrl;
    String type;

    public String getMessageType(){ return type ;}

    public String getMessageId() {
        return messageId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getSender() {
        return sender;
    }

    public String getImageUrl(){ return imageUrl; }

    public String getTime() {
        return time;
    }

    public void setMessageType(String type){ this.type = type; }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
