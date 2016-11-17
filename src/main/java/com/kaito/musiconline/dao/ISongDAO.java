package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Song;

public interface ISongDAO {
	//Table name of songs database
	public static final String TABLE_NAME = "songs";
	
	//All columns of table songs
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String GENRE = "genre";
	public static final String LYRIC = "lyric";
	public static final String PATH = "path";
	public static final String ARTIST = "artist";
	public static final String AUTHOR = "author";
	public static final String UPLOADED = "uploaded";
	public static final String VIEW = "view";
	
	public List<Song> getAllSongs();
	public Song getSong(int id);
	public Song addSong(Song song);
	public boolean updateSong(Song song);
	public boolean deleteSong(int id);
	public List<Song> searchSongsByName(String name);
	public List<Song> searchSongsByArtist(int artistId);
	public List<Song> searchSongsByGenre(int genreId);
	public List<Song> getTopSong(int top);
	
}
