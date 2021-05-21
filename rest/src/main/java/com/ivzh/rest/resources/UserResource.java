package com.ivzh.rest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ivzh.rest.dao.UserDAO;
import com.ivzh.rest.dtos.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private int defaultSize;
	private UserDAO dao;

	public UserResource(int defaultSize, UserDAO dao) {
		this.defaultSize = defaultSize;
		this.dao = dao;
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> all(@QueryParam("name") Optional<String> name) {
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
}
