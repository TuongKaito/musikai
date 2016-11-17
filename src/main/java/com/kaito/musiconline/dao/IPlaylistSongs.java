package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Song;

public interface IPlaylistSongs {

	//Table name of playlist_songs database
	public static final String TABLE_NAME = "playlist_songs";
	
	//All columns of table playlist_songs
	public static final String PLAYLIST_ID = "playlist_id";
	public static final String SONG_ID = "song_id";
	
	public List<Song> getSongsOfPlaylist(int playlistId);
	public List<Song> addSongToPlaylist(int playlistId, int songId);
	public List<Song> deleteSongFromPlaylist(int playlistId, int songId);
	
}
