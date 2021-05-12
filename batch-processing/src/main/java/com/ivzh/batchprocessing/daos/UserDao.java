package com.ivzh.batchprocessing.daos;

import com.ivzh.shared.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public PageImpl<User> findAll(Pageable page) {
        int total = count();
        String query = String.format("select * from users limit %d offset %d", page.getPageSize(), page.getOffset());
        List<User> users = jdbcTemplate.query(query, (rs, rowNum) -> mapUserResult(rs));
        return new PageImpl<>(users, page, total);
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users", Integer.class);
    }

    private User mapUserResult(final ResultSet rs) throws SQLException {
        User user = new User(rs.getString("first_name"), rs.getString("last_name"));
        return user;
    }
}
