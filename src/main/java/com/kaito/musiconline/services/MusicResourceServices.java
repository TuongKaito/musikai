package com.kaito.musiconline.services;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.MusicExtensionException;
import com.kaito.musiconline.model.MediaStreamer;
import com.kaito.musiconline.model.Song;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/musics")
public class MusicResourceServices {

	@Context ServletContext servletContext;
    @Context
    private UriInfo context;
    
    @POST
    @Path("/{username}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("username") String username) {
		
		String fileName = fileDetail.getFileName();
        MediaStreamer mStreamer = new MediaStreamer();
        //Get path images or musics
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String output = "";
        if (Resources.isFile(ext, Resources.MUSICS_EXTENSION)) {
	        String filePath = Resources.MUSIC_PATH;
	        // Path of music user upload should be MUSIC_PATH/username/music.mp3
			String rootPath = Resources.getPath(servletContext, String.format("%s/%s", 
					filePath, username));
			
	        String uploadedFileLocation = rootPath + "/" + fileName;
	        // save it
	        mStreamer.writeToFile(uploadedInputStream, uploadedFileLocation);
	        output = uploadedFileLocation;
        }
        else {
        	throw new MusicExtensionException(fileName + " is not a music file");
        }
        return Response.status(200).entity(output).build();
    }
	
	SongDAO songDao = new SongDAO();
	
	@Path("/streaming/{id}")
    @GET
    @Produces({"audio/mpeg"})
    public Response getMusic(@PathParam("id") int id, @HeaderParam("Range") String range) throws Exception {
        Song song = songDao.getSong(id);
        String rootPath = Resources.getPath(servletContext, Resources.MUSIC_PATH);
        String path = rootPath + "/" + song.getPath();
		return Resources.buildStream(new File(path), range);
    }
    
    @Path("download/{id}")
    @GET
    @Produces({"audio/mpeg"})
    public Response downloadMusic(@PathParam("id") int id) {
    	Song song = songDao.getSong(id);
    	String path = song.getPath();
        File file = new File(path);
        String fileName = song.getName() + ".mp3";
    	fileName = path.substring(path.lastIndexOf("/"));
        ResponseBuilder responseBuilder = Response.ok((Object)file);
        responseBuilder.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return responseBuilder.build();
    }	
	
}
