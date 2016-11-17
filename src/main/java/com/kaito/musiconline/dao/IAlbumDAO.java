package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Album;

public interface IAlbumDAO {

	//Table name of album database
	public static final String TABLE_NAME = "albums";
	
	//All columns of table albums;
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ARTIST = "artist";
	public static final String CREATED = "created";
	public static final String VIEW = "view";
	public static final String PHOTO = "photo";
	
	public List<Album> getAllAlbums();
	public Album getAlbum(int id);
	public Album addAlbum(Album album);
	public boolean updateAlbum(Album album);
	public boolean deleteAlbum(int id);
	public List<Album> searchAlbumsByName(String name);
	public List<Album> searchAlbumsByArtist(int artistId);
	public List<Album> getTopAlbums(int top);
	public List<Album> getLatestAlbums(int top);
	
}
