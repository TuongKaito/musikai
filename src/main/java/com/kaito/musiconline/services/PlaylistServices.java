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

import com.kaito.musiconline.daoimp.CommentDAO;
import com.kaito.musiconline.daoimp.PlaylistDAO;
import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.ImageExtensionException;
import com.kaito.musiconline.exceptions.PlaylistNotFoundException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.model.Comment;
import com.kaito.musiconline.model.CommentType;
import com.kaito.musiconline.model.MediaStreamer;
import com.kaito.musiconline.model.Playlist;
import com.kaito.musiconline.model.Song;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/playlists")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class PlaylistServices {

	@Context ServletContext servletContext;
	PlaylistDAO playlistDao = new PlaylistDAO();
	SongDAO songDao = new SongDAO();
	CommentDAO commentDao = new CommentDAO();
	
	@GET
	public List<Playlist> getAllPlaylists() {
		List<Playlist> listOfPlaylists = playlistDao.getAllPlaylists();
		return listOfPlaylists;
	}
	
	@GET
	@Path("/{id}")
	public Playlist getPlaylistById(@PathParam("id") int id) {
		Playlist playlist = playlistDao.getPlaylist(id);
		return playlist;
	}
	
	@GET
	@Path("/{id}/songs")
	public List<Song> getSongsOfPlaylist(@PathParam("id") int id) {
		List<Song> listOfSongs = songDao.getSongsOfPlaylist(id);
		return listOfSongs;
	}
	
	@GET
	@Path("{id}/comments")
	public List<Comment> getCommentsOfPlaylist(@PathParam("id") int playlistId) {
		List<Comment> listOfComments = commentDao.getCommentsOfType(playlistId, CommentType.PLAYLIST);
		return listOfComments;
	}
	
	@GET
	@Path("/top")
	public List<Playlist> getTopPlaylists(@QueryParam("size") int size) {
		List<Playlist> listOfPlaylists = playlistDao.getTopPlaylists(size);
		return listOfPlaylists;
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
	        String filePath = Resources.PLAYLIST_PATH;
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
	public Playlist addNewPlaylist(Playlist playlist) {
		playlist = playlistDao.addPlaylist(playlist);
		return playlist;
	}
	
	@PUT
	@Path("/{id}")
	public Response updatePlaylist(Playlist playlist, @PathParam("id") int id) {
		if (playlist.getId() != id) {
			throw new PlaylistNotFoundException("The playlist id(" + playlist.getId() + ") is not match with resource path");
		}
		if (!playlistDao.updatePlaylist(playlist)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deletePlaylist(@PathParam("id") int id) {
		if (!playlistDao.deletePlaylist(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@Path("/{id}/photo")
    @GET
    @Produces({"image/jpg", "image/png", "image/gif", "image/bmp", "image/jpeg"})
    public Response getPhoto(@PathParam("id") int id, @HeaderParam("Range") String range) throws Exception {
        Playlist playlist = playlistDao.getPlaylist(id);
        if (playlist == null) {
        	throw new PlaylistNotFoundException("The playlist(" + id + ") is not found");
        }
        String rootPath = Resources.getPath(servletContext, Resources.PLAYLIST_PATH + "/" + id);
        String path = rootPath + "/" + playlist.getPhoto();
		return Resources.buildStream(new File(path), range);
    }
	
}

