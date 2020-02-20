package com.example.Caps;

public class MyChat {
    private String chatId;
    private String chatMessage;
    private String chatSender;
    private String chatReceiver;
    private String chatTime;

    public MyChat() {

    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getChatSender() {
        return chatSender;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public String getChatReceiver() {
        return chatReceiver;
    }

    public void setChatReceiver(String chatReceiver) {
        this.chatReceiver = chatReceiver;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
