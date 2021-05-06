package com.ivzh.batchprocessing.notifications;

public interface SimpleNotificationStrategy {
    void send(final String from, final String to, final String subject, final String text);
}
