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
import javax.ws.rs.core.Response;

import com.kaito.musiconline.daoimp.GenreDAO;
import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.GenreNotFoundException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.model.Genre;
import com.kaito.musiconline.model.Song;

@Path("/genres")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class GenreServices {

	GenreDAO genreDao = new GenreDAO();
	SongDAO songDao = new SongDAO();
	
	@GET
	public List<Genre> getAllGenres() {
		List<Genre> listOfGenres = genreDao.getAllGenres();
		return listOfGenres;
	}
	
	@GET
	@Path("/{id}")
	public Genre getGenre(@PathParam("id") int id) {
		Genre genre = genreDao.getGenre(id);
		return genre;
	}
	
	@GET
	@Path("/{id}/songs") 
	public List<Song> getSongsOfGenre(@PathParam("id") int id) {
		List<Song> listOfSongs = songDao.searchSongsByGenre(id);
		return listOfSongs;
	}
	
	@POST
	public Genre addNewGenre(Genre genre) {
		genre = genreDao.addGenre(genre);
		if (genre == null) {
			throw new ServicesException();
		}
		return genre;
	}
	
	@PUT
	@Path("/{id}")
	public Genre updateGenre(Genre genre, @PathParam("id") int id) {
		if (genre.getId() != id) {
			throw new GenreNotFoundException("The genre id(" + genre.getId() + ") is not match with resource path");
		}
		if (!genreDao.updateGenre(genre)) {
			throw new ServicesException();
		}
		return genre;
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteGenre(@PathParam("id") int id) {
		if (!genreDao.deleteGenre(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
}
