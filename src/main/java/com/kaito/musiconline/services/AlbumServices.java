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

import com.kaito.musiconline.daoimp.AlbumDAO;
import com.kaito.musiconline.daoimp.CommentDAO;
import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.AlbumNotFoundException;
import com.kaito.musiconline.exceptions.ImageExtensionException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.model.Album;
import com.kaito.musiconline.model.Comment;
import com.kaito.musiconline.model.CommentType;
import com.kaito.musiconline.model.MediaStreamer;
import com.kaito.musiconline.model.Song;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/albums")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class AlbumServices {

	@Context ServletContext servletContext;
	AlbumDAO albumDao = new AlbumDAO();
	SongDAO songDao = new SongDAO();
	CommentDAO commentDao = new CommentDAO();
	
	@GET
	public List<Album> getAllAlbums() {
		List<Album> listOfAlbums = albumDao.getAllAlbums();
		return listOfAlbums;
	}
	
	@GET
	@Path("/{id}")
	public Album getAlbumById(@PathParam("id") int id) {
		Album album = albumDao.getAlbum(id);
		return album;
	}
	
	@GET
	@Path("/{id}/songs")
	public List<Song> getSongsOfAlbum(@PathParam("id") int id) {
		List<Song> listOfSongs = songDao.getSongsOfAlbum(id);
		return listOfSongs;
	}
	
	@GET
	@Path("{id}/comments")
	public List<Comment> getCommentsOfAlbum(@PathParam("id") int albumId) {
		List<Comment> listOfComments = commentDao.getCommentsOfType(albumId, CommentType.ALBUM);
		return listOfComments;
	}
	
	@GET
	@Path("/top")
	public List<Album> getTopAlbums(@QueryParam("size") int size) {
		List<Album> listOfAlbums = albumDao.getTopAlbums(size);
		return listOfAlbums;
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
	        String filePath = Resources.ALBUM_PATH;
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
	public Album addNewAlbum(Album album) {
		album = albumDao.addAlbum(album);
		return album;
	}
	
	@PUT
	@Path("/{id}")
	public Response updateAlbum(Album album, @PathParam("id") int id) {
		if (album.getId() != id) {
			throw new AlbumNotFoundException("The album id(" + album.getId() + ") is not match with resource path");
		}
		if (!albumDao.updateAlbum(album)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteAlbum(@PathParam("id") int id) {
		if (!albumDao.deleteAlbum(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@Path("/{id}/photo")
    @GET
    @Produces({"image/jpg", "image/png", "image/gif", "image/bmp", "image/jpeg"})
    public Response getPhoto(@PathParam("id") int id, @HeaderParam("Range") String range) throws Exception {
        Album album = albumDao.getAlbum(id);
        if (album == null) {
        	throw new AlbumNotFoundException("The album(" + id + ") is not found");
        }
        String rootPath = Resources.getPath(servletContext, Resources.ALBUM_PATH + "/" + id);
        String path = rootPath + "/" + album.getPhoto();
		return Resources.buildStream(new File(path), range);
    }
	
}
