package com.ivzh.batchprocessing.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DefaultNotificationGateway {

    private SimpleNotificationStrategy defaultNotificationGateway;

    @Value("${default.notification.strategy}")
    private String defaultNotificationStrategy;
    @Autowired
    private NotificationStrategiesSwitcher notificationStrategiesSwitcher;


    public SimpleNotificationStrategy getDefaultSimpleNotificationGateway() {
        return defaultNotificationGateway;
    }

    @PostConstruct
    public void init() {
        defaultNotificationGateway = notificationStrategiesSwitcher.switchStrategy(defaultNotificationStrategy);
    }
}
