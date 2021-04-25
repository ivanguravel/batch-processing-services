package com.ivzh.rest.dao;

import com.ivzh.rest.dao.mappers.UserMapper;
import com.ivzh.rest.dtos.User;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.Arrays;
import java.util.List;

public interface UserDAO {
	@SqlQuery("select * from users")
	@Mapper(UserMapper.class)
	List<User> getUsers();
}
