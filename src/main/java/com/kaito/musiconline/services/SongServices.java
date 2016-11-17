package com.kaito.musiconline.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.kaito.musiconline.daoimp.CommentDAO;
import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.exceptions.SongNotFoundException;
import com.kaito.musiconline.model.Comment;
import com.kaito.musiconline.model.CommentType;
import com.kaito.musiconline.model.Song;

@Path("/songs")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class SongServices {

	SongDAO songDao = new SongDAO();
	CommentDAO commentDao = new CommentDAO();
	
	@GET
	public List<Song> getAllSongs() {
		List<Song> listOfSongs = songDao.getAllSongs();
		return listOfSongs;
	}
	
	@GET
	@Path("/{id}")
	public Song getSongById(@PathParam("id") int id) {
		Song song = songDao.getSong(id);
		if (song == null) {
			throw new SongNotFoundException("The song id(" + id + ") is not found");
		}
		return song;
	}
	
	@GET
	@Path("/song")
	public List<Song> getSongsByName(@QueryParam("name") String name) {
		List<Song> listOfSongs = null;
		if (name == null) {
			throw new SongNotFoundException("You wanna get songs with query string '?name=songName'");
		}
		listOfSongs = songDao.searchSongsByName(name);
		return listOfSongs;
	}
	
	@GET
	@Path("/{id}/comments")
	public List<Comment> getAllComments(@PathParam("id") int songId) {
		List<Comment> listOfComments = commentDao.getCommentsOfType(songId, CommentType.SONG);
		return listOfComments;
	}
	
	@GET
	@Path("/top")
	public List<Song> getTopSongs(@QueryParam("size") int size) {
		List<Song> listOfSongs = null;
		if (size < 1) {
			throw new SongNotFoundException("You wanna get top songs with query string '?size=topSize'");
		}
		listOfSongs = songDao.getTopSong(size);
		return listOfSongs;
	}
	
	@POST
	public Response addNewSong(Song song) {
		if (song == null) {
			throw new SongNotFoundException("The song to add is not exist");
		}
		song = songDao.addSong(song);
		if (song == null) {
			throw new ServicesException();
		}
		return Response.status(Status.CREATED).entity(song).build();
	}

	@PUT
	@Path("/{id}")
	public Response updateSong(Song song, @PathParam("id") int id) {
		if (song.getId() != id) {
			throw new SongNotFoundException("The song with id(" + song.getId() + ") not match with resource path");
		}
		else if (!songDao.updateSong(song)) {
			throw new SongNotFoundException("Cannot update song with id = " + id);
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteSong(@PathParam("id") int id)  {
		if (songDao.deleteSong(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	
	
}
