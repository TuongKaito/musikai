package com.kaito.musiconline.services;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.kaito.musiconline.daoimp.UserDAO;
import com.kaito.musiconline.exceptions.ImageExtensionException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.exceptions.UserNotFoundException;
import com.kaito.musiconline.model.MediaStreamer;
import com.kaito.musiconline.model.User;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/users")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class UserServices {

	@Context ServletContext servletContext;
	UserDAO userDao = new UserDAO();
	
	@GET
	public List<User> getAllUsers() {
		List<User> listOfUsers = userDao.getAllUsers();
		return listOfUsers;
	}
	
	@GET
	@Path("/{id}")
	public Response getUserById(@PathParam("id") int id) {
		User user = userDao.getUser(id);
		if (user == null) {
			throw new ServicesException();
		}
		return Response.ok(user).build();
	}
	
	@GET
	@Path("/user")
	public List<User> getUsersByName(@QueryParam("name") String name) {
		List<User> listOfUsers = userDao.searchUsersByName(name);
		return listOfUsers;
	}
	
	@POST
    @Path("/{id}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("id") int id) {
		
		String fileName = fileDetail.getFileName();
        MediaStreamer mStreamer = new MediaStreamer();
        //Get path images or musics
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String output = "";
        if (Resources.isFile(ext, Resources.IMAGES_EXTENSION)) {
	        String filePath = Resources.AVATAR_PATH;
	        // Path of album user upload should be ALBUM_PATH/id/album.*
			String rootPath = Resources.getPath(servletContext, String.format("%s/%s", 
					filePath, id));
			
	        String uploadedFileLocation = rootPath + "/" + fileName;
	        // save it
	        mStreamer.writeToFile(uploadedInputStream, uploadedFileLocation);
	        output = uploadedFileLocation;
        }
        else {
        	throw new ImageExtensionException(fileName + " is not a image file");
        }
        return Response.status(200).entity(output).build();
    }
	
	@POST
	public Response addNewUser(User user) {
		user = userDao.addUser(user);
		if (user == null) {
			throw new ServicesException();
		}
		return Response.status(Status.CREATED).entity(user).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response updateUser(User user, @PathParam("id") int id) {
		if (user.getId() != id) {
			throw new UserNotFoundException("The user with id(" + user.getId() + ") not match with resource path");
		}
		if (!userDao.updateUser(user)) {
			throw new ServicesException();
		}
		return Response.ok(user).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") int id) {
		if (!userDao.deleteUser(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@Path("/{id}/photo")
    @GET
    @Produces({"image/jpg", "image/png", "image/gif", "image/bmp", "image/jpeg"})
    public Response getPhoto(@PathParam("id") int id, @HeaderParam("Range") String range) throws Exception {
        User user = userDao.getUser(id);
        if (user == null) {
        	throw new UserNotFoundException("The user(" + id + ") is not found");
        }
        String rootPath = Resources.getPath(servletContext, Resources.AVATAR_PATH + "/" + id);
        String path = rootPath + "/" + user.getPhoto();
		return Resources.buildStream(new File(path), range);
    }
	
}
