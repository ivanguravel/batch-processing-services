package com.ivzh.batchprocessing.notifications;

import com.ivzh.batchprocessing.utils.StrategiesSwitcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationStrategiesSwitcher extends StrategiesSwitcher<SimpleNotificationStrategy> {
    @Autowired
    public NotificationStrategiesSwitcher(final Map<String, SimpleNotificationStrategy> strategies) {
        super.strategyMap = strategies;
    }
}
