package com.kaito.musiconline.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class MusicExtensionException extends WebApplicationException {

	private static final long serialVersionUID = 10L;
	
	public MusicExtensionException(String message) {
		super(Response.status(Status.UNSUPPORTED_MEDIA_TYPE).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
	}

}
