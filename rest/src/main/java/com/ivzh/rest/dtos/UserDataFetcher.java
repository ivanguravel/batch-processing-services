package com.ivzh.rest.dtos;

import com.ivzh.rest.dao.UserDAO;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import liquibase.pro.packaged.U;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDataFetcher implements DataFetcher<User> {

    private UserDAO dao;

    public UserDataFetcher(UserDAO dao) {
        this.dao = dao;
    }

    @Override
    public User get(DataFetchingEnvironment environment) {
        Optional<String> name = Optional.ofNullable(environment.getArgument("name"));
        return name.isPresent() ? new User("a", "a") : new User(name.get(), name.get());

//        List<User> users = dao.getUsers();
//        if (name.isPresent()) {
//            String extractedName = name.get();
//            return users.stream()
//                    .filter(user -> user.getFirstName().contains(extractedName) || user.getLastName().contains(extractedName))
//                    .collect(Collectors.toList());
//        } else {
//            return users;
//        }
    }
}
