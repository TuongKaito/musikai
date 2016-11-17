package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Artist;

public interface IArtistDAO {

	//Table name of artist database
	public static final String TABLE_NAME = "artist";
	
	//All columns of table artist
	public static final String ID = "id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String GENDER = "gender";
	public static final String DATE_OF_BIRTH = "date_of_birth";
	public static final String PLACE_OF_BIRTH = "place_of_birth";
	public static final String GENRE = "genre";
	public static final String PROFILE = "profile";
	public static final String LIKE = "_like";
	public static final String PHOTO = "photo";
	
	public List<Artist> getAllArtists();
	public Artist getArtist(int id);
	public Artist addArtist(Artist artist);
	public boolean updateArtist(Artist artist);
	public boolean deleteArtist(int id);
	public List<Artist> searchArtistsByName(String name);
	public List<Artist> searchArtistsByGenre(int genreId);
	//Return artist the highest like
	public List<Artist> getTopFamousArtist(int top);
	
}
