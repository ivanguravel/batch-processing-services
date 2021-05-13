package com.ivzh.rest.dtos;

import com.ivzh.rest.dao.UserDAO;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDataFetcher implements DataFetcher<List<User>> {

    private UserDAO dao;

    public UserDataFetcher() {
    }

    @Override
    public List<User> get(DataFetchingEnvironment environment) {
        Optional<String> name = Optional.ofNullable(environment.getArgument("name"));

        List<User> users = dao.getUsers();
        if (name.isPresent()) {
            String extractedName = name.get();
            return users.stream()
                    .filter(user -> user.getFirstName().contains(extractedName) || user.getLastName().contains(extractedName))
                    .collect(Collectors.toList());
        } else {
            return users;
        }
    }

    public void setDao(UserDAO dao) {
        this.dao = dao;
    }
}
