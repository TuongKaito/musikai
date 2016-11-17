package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Genre;

public interface IGenreDAO {

	//Table name of genres database
	public static final String TABLE_NAME = "genres";
	
	//All columns of table genres
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	public List<Genre> getAllGenres();
	public Genre getGenre(int id);
	public Genre addGenre(Genre genre);
	public boolean updateGenre(Genre genre);
	public boolean deleteGenre(int id);
	
}
