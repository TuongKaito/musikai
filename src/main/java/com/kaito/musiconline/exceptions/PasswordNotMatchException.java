package com.kaito.musiconline.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class PasswordNotMatchException extends WebApplicationException {

	private static final long serialVersionUID = 3L;

	public PasswordNotMatchException(String message) {
		super(Response.status(Status.BAD_REQUEST).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
	}
}
