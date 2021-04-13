package com.ivzh.rest.dao;

import com.ivzh.rest.dtos.User;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.List;

public interface UserDAO {
	@SqlQuery("select * from users")
	List<User> getUser();
}
