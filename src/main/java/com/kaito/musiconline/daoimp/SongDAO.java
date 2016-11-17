package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.IAlbumSongs;
import com.kaito.musiconline.dao.IPlaylistSongs;
import com.kaito.musiconline.dao.ISongDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Song;

public class SongDAO implements ISongDAO, IAlbumSongs, IPlaylistSongs {

	DBService db;
	
	public SongDAO() {
		db = new DBService();
	}
	
	public List<Song> getAllSongs() {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", ISongDAO.TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Song getSong(int id) {
		Song song = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", ISongDAO.TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				song = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return song;
	}
	
	public Song addSong(Song song) {
		Song newSong = null;
		try {
			db.open();
			//Query has 3 default values id, uploaded and view
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, ?, ?, ?, ?, default, default);", ISongDAO.TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, song);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			newSong = getSong(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return newSong;
	}
	
	public boolean updateSong(Song song) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, "
					+ "%s=?, %s=? WHERE %s=?", ISongDAO.TABLE_NAME, NAME, GENRE, LYRIC, PATH,
					ARTIST, AUTHOR, UPLOADED, VIEW, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 6 parameters for prepared statement except id, uploaded and view
			setParameters(db.prepStatement, song);
			//Set uploaded parameter at index 7, view parameter at index 8 and id parameter at index 9
			db.prepStatement.setDate(7, new java.sql.Date(song.getUploaded().getTime()));
			db.prepStatement.setLong(8, song.getView());
			db.prepStatement.setInt(9, song.getId());
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected > 0)
				success = true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public boolean deleteSong(int id) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("DELETE FROM %s WHERE %s = ?;", ISongDAO.TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected > 0) {
				success= true;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public List<Song> searchSongsByName(String name) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s like ?;", 
					ISongDAO.TABLE_NAME, NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, "%" + name + "%");
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Song> searchSongsByArtist(int artistId) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s=?;", 
					ISongDAO.TABLE_NAME, ARTIST);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, artistId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Song> searchSongsByGenre(int genreId) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s=?;", 
					ISongDAO.TABLE_NAME, GENRE);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, genreId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Song> getTopSong(int top) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC, %s LIMIT 0, %s;",
					ISongDAO.TABLE_NAME, VIEW, ID, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	private void setParameters(PreparedStatement prep, Song song) throws SQLException {
		prep.setNString(1, song.getName());
		prep.setInt(2, song.getGenre());
		prep.setNString(3, song.getLyric());
		prep.setString(4, song.getPath());
		prep.setInt(5, song.getArtistId());
		prep.setInt(6, song.getAuthorId());
	}
	
	private Song fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String name = rs.getNString(NAME);
		int genre = rs.getInt(GENRE);
		String lyric = rs.getNString(LYRIC);
		String path = rs.getString(PATH);
		int artistId = rs.getInt(ARTIST);
		int authorId = rs.getInt(AUTHOR);
		Date uploaded = rs.getDate(UPLOADED);
		long view = rs.getLong(VIEW);
		Song song = new Song(id, name, genre, lyric, path, artistId, authorId, uploaded, view);
		return song;
	}

	// IAlbumSongs implements
	@Override
	public List<Song> getSongsOfAlbum(int albumId) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s S join %s A on S.%s = A.%s WHERE a.%s = %s;",
					ISongDAO.TABLE_NAME, IAlbumSongs.TABLE_NAME, ISongDAO.ID, IAlbumSongs.SONG_ID,
					IAlbumSongs.ALBUM_ID, albumId);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}

	@Override
	public List<Song> addSongToAlbum(int albumId, int songId) {
		try {
			db.open();
			String query = String.format("INSERT INTO %s VALUES(?, ?);", IAlbumSongs.TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, albumId);
			db.prepStatement.setInt(2, songId);
			db.prepStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		List<Song> list = getSongsOfAlbum(albumId);
		return list;
	}

	@Override
	public List<Song> deleteSongFromAlbum(int albumId, int songId) {
		try {
			db.open();
			String query = String.format("DELETE FROM %s WHERE %s = ? AND %s = ?",
					IAlbumSongs.TABLE_NAME, IAlbumSongs.ALBUM_ID, IAlbumSongs.SONG_ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, albumId);
			db.prepStatement.setInt(2, songId);
			db.prepStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		List<Song> list = getSongsOfAlbum(albumId);
		return list;
	}
	
	// IPlaylistSongs implements
	@Override
	public List<Song> getSongsOfPlaylist(int playlistId) {
		List<Song> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s S join %s A on S.%s = A.%s WHERE a.%s = %s;",
					ISongDAO.TABLE_NAME, IPlaylistSongs.TABLE_NAME, ISongDAO.ID, IPlaylistSongs.SONG_ID,
					IPlaylistSongs.PLAYLIST_ID, playlistId);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Song song = fetchResultSet(db.resultSet);
				list.add(song);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}

	@Override
	public List<Song> addSongToPlaylist(int playlistId, int songId) {
		try {
			db.open();
			String query = String.format("INSERT INTO %s VALUES(?, ?);", IPlaylistSongs.TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, playlistId);
			db.prepStatement.setInt(2, songId);
			db.prepStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		List<Song> list = getSongsOfAlbum(playlistId);
		return list;
	}

	@Override
	public List<Song> deleteSongFromPlaylist(int playlistId, int songId) {
		try {
			db.open();
			String query = String.format("DELETE FROM %s WHERE %s = ? AND %s = ?",
					IPlaylistSongs.TABLE_NAME, IPlaylistSongs.PLAYLIST_ID, IPlaylistSongs.SONG_ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, playlistId);
			db.prepStatement.setInt(2, songId);
			db.prepStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		List<Song> list = getSongsOfAlbum(playlistId);
		return list;
	}
}
