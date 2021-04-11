package com.ivzh.rest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ivzh.rest.dao.UserDAO;
import com.sun.jersey.api.client.*;
import com.ivzh.rest.representations.User;
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
	String url = "http://localhost:8080/contact/";
	public void setWebResource(WebResource contactResource) {
		this.contactResource = contactResource;
	}


	@GET
    @Path("getAllUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAllContact() {
        contactResource = client.resource(url + "all");
        return contactResource.get(Response.class);

    }
}
