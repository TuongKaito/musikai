package com.kaito.musiconline.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ServicesException extends WebApplicationException {

	private static final long serialVersionUID = 0L;
	
	public ServicesException() {
		super(Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Service has some error").type(MediaType.TEXT_PLAIN).build());
	}

}
