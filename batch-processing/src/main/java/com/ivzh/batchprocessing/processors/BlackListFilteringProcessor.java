package com.ivzh.batchprocessing.processors;

import com.ivzh.batchprocessing.notifications.DefaultNotificationGateway;
import com.ivzh.batchprocessing.notifications.NotificationStrategiesSwitcher;
import com.ivzh.batchprocessing.notifications.SimpleNotificationStrategy;
import com.ivzh.shared.dtos.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

public class BlackListFilteringProcessor implements ItemProcessor<User, User> {

    Set<User> blackListUsers;


    @Autowired
    private DefaultNotificationGateway defaultNotificationGateway;

    public BlackListFilteringProcessor() {
        this.blackListUsers = new HashSet<>();
        blackListUsers.add(new User("ivzh", "ivzh"));
    }

    @Override
    public User process(User user) {
        if (blackListUsers.contains(user)) {
            defaultNotificationGateway.getDefaultSimpleNotificationGateway()
                    .send("app@test.com", "admin@test.com", "blocked user login attempt", "some template message");
            throw new IllegalArgumentException(String.format("can't process %s user because of blacklist", user));
        } else {
            return user;
        }
    }

}
