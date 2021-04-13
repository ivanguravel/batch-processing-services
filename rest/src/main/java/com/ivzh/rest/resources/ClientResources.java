package com.ivzh.rest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ivzh.rest.dao.UserDAO;
import com.ivzh.rest.dtos.User;
import com.sun.jersey.api.client.*;

import java.util.List;

@Produces(MediaType.TEXT_PLAIN)
@Path("/client/")
public class ClientResources {
	private ClientResponse response;
	private WebResource contactResource;
	private Client client;
	private UserDAO dao;
	public ClientResources(Client client, UserDAO dao) {
		this.dao = dao;
		this.client = client;
	}
	String url = "http://localhost:8080/user/";
	public void setWebResource(WebResource contactResource) {
		this.contactResource = contactResource;
	}


	@GET
    @Path("getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> all() {
        return dao.getUser();
    }
}
