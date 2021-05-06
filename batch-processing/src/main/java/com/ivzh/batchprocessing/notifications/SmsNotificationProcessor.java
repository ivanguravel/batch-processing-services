package com.ivzh.batchprocessing.notifications;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("sms")
public class SmsNotificationProcessor implements SimpleNotificationStrategy {
    @Async
    @Override
    public void send(String from, String to, String subject, String text) {
        System.out.println("sms delivery");
    }
}
