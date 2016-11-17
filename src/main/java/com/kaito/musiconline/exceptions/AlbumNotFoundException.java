package com.kaito.musiconline.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AlbumNotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 7L;

	public AlbumNotFoundException(String message) {
		super(Response.status(Status.NOT_FOUND).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
	}
	
}
