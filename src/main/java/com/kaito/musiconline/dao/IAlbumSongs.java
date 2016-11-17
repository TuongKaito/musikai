package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Song;

public interface IAlbumSongs {

	//Table name of album_songs database
	public static final String TABLE_NAME = "album_songs";
	
	//All columns of table album_songs
	public static final String ALBUM_ID = "album_id";
	public static final String SONG_ID = "song_id";
	
	public List<Song> getSongsOfAlbum(int albumId);
	public List<Song> addSongToAlbum(int albumId, int songId);
	public List<Song> deleteSongFromAlbum(int albumId, int songId);
	
}
