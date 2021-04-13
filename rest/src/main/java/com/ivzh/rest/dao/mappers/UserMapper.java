package com.ivzh.rest.dao.mappers;


import com.ivzh.rest.dtos.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.*;

public class UserMapper implements ResultSetMapper<User> {

    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        return new User(
                r.getString("first_name"),
                r.getString("last_name")
        );
    }
}
