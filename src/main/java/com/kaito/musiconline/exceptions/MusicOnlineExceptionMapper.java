package com.kaito.musiconline.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class MusicOnlineExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception e) {
		ResourceError resourceError = new ResourceError(e.getMessage());
		
		if (e instanceof UserNotFoundException || e instanceof SongNotFoundException ||
			e instanceof AccountNotFoundException) {
			resourceError.setCode(Response.Status.NOT_FOUND.getStatusCode());
			return Response.status(Response.Status.NOT_FOUND).entity(resourceError)
					.type(MediaType.APPLICATION_JSON_TYPE).build();
		}
		else if (e instanceof PasswordNotMatchException) {
			resourceError.setCode(Response.Status.BAD_REQUEST.getStatusCode());
			return Response.status(Response.Status.BAD_REQUEST).entity(resourceError)
					.type(MediaType.APPLICATION_JSON_TYPE).build();
		}
		resourceError.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
		resourceError.setMessage("Service encountered an internal error");
		return Response.status(503).entity(resourceError)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
	}
}
