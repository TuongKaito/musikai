package com.kaito.musiconline.services;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.kaito.musiconline.exceptions.MusicExtensionException;
import com.kaito.musiconline.model.MediaStreamer;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/uploads")
public class FileUpLoadServices {

	@Context ServletContext servletContext;
	
	
	
}
