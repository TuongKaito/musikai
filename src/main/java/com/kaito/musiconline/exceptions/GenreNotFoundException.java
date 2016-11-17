package com.kaito.musiconline.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class GenreNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 5L;
	
	public GenreNotFoundException(String message) {
		super(Response.status(Status.NOT_FOUND).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
	}

}
