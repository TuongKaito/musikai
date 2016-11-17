package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Playlist;

public interface IPlaylistDAO {

	//Table name of playlist database
	public static final String TABLE_NAME = "playlist";
	
	//All columns of table playlist
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CREATED = "created";
	public static final String AUTHOR = "author";
	public static final String VIEW = "view";
	public static final String PHOTO = "photo";
	
	public List<Playlist> getAllPlaylists();
	public Playlist getPlaylist(int id);
	public Playlist addPlaylist(Playlist playlist);
	public boolean updatePlaylist(Playlist playlist);
	public boolean deletePlaylist(int id);
	public List<Playlist> searchPlaylistsByName(String name);
	public List<Playlist> searchPlaylistsByAuthor(int id);
	public List<Playlist> getTopPlaylists(int top);
	public List<Playlist> getLatestPlaylists(int top);
	
}
