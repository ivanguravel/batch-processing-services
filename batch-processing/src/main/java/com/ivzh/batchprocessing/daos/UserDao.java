package com.ivzh.batchprocessing.daos;

import com.ivzh.shared.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<User> findAll(Pageable page) {
        int total = count();
        String query = "";
        return null;
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users", Integer.class);
    }


}
