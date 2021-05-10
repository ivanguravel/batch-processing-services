package com.ivzh.batchprocessing.daos;

import com.ivzh.shared.dtos.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HeaderDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveBatch(List<Header> headers) {
        for (Header header : headers) {
            jdbcTemplate.batchUpdate("INSERT INTO headers(name, count) VALUES (?,?)", header.getName(), Long.toString(header.getCount()));
        }
    }
}
