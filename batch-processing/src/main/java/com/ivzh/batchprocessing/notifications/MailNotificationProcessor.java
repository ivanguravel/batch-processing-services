package com.ivzh.batchprocessing.notifications;

import org.springframework.scheduling.annotation.Async;

public class MailNotificationProcessor implements SimpleNotificationStrategy {
    @Async
    @Override
    public void send(String from, String to, String subject, String text) {
        System.out.println("mail delivery");
    }
}
