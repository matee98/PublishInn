package com.github.PublishInn.utils;

public interface EmailSender {
    void send(String to, String emailContent, String emailSubject);
}
