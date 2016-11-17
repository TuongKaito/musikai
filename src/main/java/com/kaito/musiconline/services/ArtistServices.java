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

import com.kaito.musiconline.daoimp.ArtistDAO;
import com.kaito.musiconline.daoimp.SongDAO;
import com.kaito.musiconline.exceptions.ArtistNotFoundException;
import com.kaito.musiconline.exceptions.ImageExtensionException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.model.Artist;
import com.kaito.musiconline.model.MediaStreamer;
import com.kaito.musiconline.model.Song;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/artists")
@Consumes(Resources.JSON)
@Produces(Resources.JSON)
public class ArtistServices {

	@Context ServletContext servletContext;
	ArtistDAO artistDao = new ArtistDAO();
	SongDAO songDao = new SongDAO();
	
	@GET
	public List<Artist> getAllArtists() {
		List<Artist> listOfArtists = artistDao.getAllArtists();
		return listOfArtists;
	}
	
	@GET
	@Path("/{id}") 
	public Artist getArtistById(@PathParam("id") int id) {
		Artist artist = artistDao.getArtist(id);
		if (artist == null) {
			throw new ArtistNotFoundException("The artist id(" + id + ") is not found");
		}
		return artist;
	}
	
	@GET
	@Path("/artist")
	public List<Artist> getArtistByName(@QueryParam("name") String name) {
		List<Artist> listOfArtists = null;
		if (name == null) {
			throw new ArtistNotFoundException("You wanna get artists with query string '?name=artistName'");
		}
		listOfArtists = artistDao.searchArtistsByName(name);
		return listOfArtists;
	}
	
	@GET
	@Path("/{id}/songs")
	public List<Song> getSongsOfArtist(@PathParam("id") int artistId) {
		List<Song> listOfSongs = songDao.searchSongsByArtist(artistId);
		return listOfSongs;
	}
	
	@GET
	@Path("/top")
	public List<Artist> getTopArtists(@QueryParam("size") int size) {
		List<Artist> listOfArtists = null;
		if (size < 1) {
			throw new ArtistNotFoundException("You wanna get top artists with query string '?size=topSize'");
		}
		listOfArtists = artistDao.getTopFamousArtist(size);
		return listOfArtists;
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
	        String filePath = Resources.ARTIST_PATH;
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
	public Artist addNewArtist(Artist artist) {
		artist = artistDao.addArtist(artist);
		if (artist == null) {
			throw new ServicesException();
		}
		return artist;
	}
	
	@PUT
	@Path("/{id}")
	public Response updateArtist(Artist artist, @PathParam("id") int id) {
		if (artist.getId() != id) {
			throw new ArtistNotFoundException("The artist id(" + artist.getId() + ") is not match with resource path");
		}
		if (!artistDao.updateArtist(artist)) {
			throw new ServicesException();
		}
		return Response.ok(artist).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteArtist(@PathParam("id") int id) {
		if (!artistDao.deleteArtist(id)) {
			throw new ServicesException();
		}
		return Response.ok().build();
	}
	
	@Path("/{id}/photo")
    @GET
    @Produces({"image/jpg", "image/png", "image/gif", "image/bmp", "image/jpeg"})
    public Response getPhoto(@PathParam("id") int id, @HeaderParam("Range") String range) throws Exception {
        Artist artist = artistDao.getArtist(id);
        if (artist == null) {
        	throw new ArtistNotFoundException("The artist(" + id + ") is not found");
        }
        String rootPath = Resources.getPath(servletContext, Resources.ARTIST_PATH + "/" + id);
        String path = rootPath + "/" + artist.getPhoto();
		return Resources.buildStream(new File(path), range);
    }

}
