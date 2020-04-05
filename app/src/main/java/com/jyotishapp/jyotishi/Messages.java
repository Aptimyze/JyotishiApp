package com.jyotishapp.jyotishi;

public class Messages {
    String textMessage;
    String time;
    String sender;
    String messageId;

    public String getMessageId() {
        return messageId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
