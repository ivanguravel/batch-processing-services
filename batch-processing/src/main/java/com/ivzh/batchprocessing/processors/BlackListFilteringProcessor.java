package com.ivzh.batchprocessing.processors;

import com.ivzh.batchprocessing.dtos.User;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class BlackListFilteringProcessor implements ItemProcessor<User, User> {

    Set<User> blackListUsers;

    public BlackListFilteringProcessor() {
        this.blackListUsers = new HashSet<>();
        blackListUsers.add(new User("ivzh", "ivzh"));
    }

    @Override
    public User process(User user) {
        if (blackListUsers.contains(user)) {
            throw new IllegalArgumentException(String.format("can't process %s user because of blacklist", user));
        } else {
            return user;
        }
    }
}
