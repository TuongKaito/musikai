package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.IPlaylistSongs;
import com.kaito.musiconline.dao.IPlaylistDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Playlist;

public class PlaylistDAO implements IPlaylistDAO {

	DBService db;
	
	public PlaylistDAO() {
		db = new DBService();
	}
	
	public List<Playlist> getAllPlaylists() {
		List<Playlist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Playlist playlist = fetchResultSet(db.resultSet);
				list.add(playlist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Playlist getPlaylist(int id) {
		Playlist playlist = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				playlist = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return playlist;
	}
	
	public Playlist addPlaylist(Playlist playlist) {
		try {
			db.open();
			//Query has 4 default values id, created, view and photo
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, default, ?, default, default);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, playlist);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			playlist.setId(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return playlist;
	}
	
	public boolean updatePlaylist(Playlist playlist) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? "
					+ "WHERE %s=?", TABLE_NAME, NAME, DESCRIPTION, AUTHOR, CREATED,
					VIEW, PHOTO, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 3 parameters for prepared statement except id, created, view and photo
			setParameters(db.prepStatement, playlist);
			//Set created parameter at index 4, view parameter at index 5,
			//photo parameter at index 6 and id parameter at index 7
			db.prepStatement.setDate(4, new java.sql.Date(playlist.getCreated().getTime()));
			db.prepStatement.setLong(5, playlist.getView());
			db.prepStatement.setString(6, playlist.getPhoto());
			db.prepStatement.setInt(7, playlist.getId());
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
	
	public boolean deletePlaylist(int id) {
		boolean success = false;
		try {
			db.open();
			//Delete all songs in playlist first
			String query = String.format("DELETE FROM %s WHERE %s = ?;", IPlaylistSongs.TABLE_NAME,
					IPlaylistSongs.PLAYLIST_ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.prepStatement.executeUpdate();
			
			//Then delete playlist
			query = String.format("DELETE FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
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
	
	public List<Playlist> searchPlaylistsByName(String name) {
		List<Playlist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s like ?;", 
					TABLE_NAME, NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, "%" + name + "%");
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Playlist playlist = fetchResultSet(db.resultSet);
				list.add(playlist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Playlist> searchPlaylistsByAuthor(int authorId) {
		List<Playlist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s=?;", 
					TABLE_NAME, AUTHOR);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, authorId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Playlist playlist = fetchResultSet(db.resultSet);
				list.add(playlist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Playlist> getTopPlaylists(int top) {
		List<Playlist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC LIMIT 0, %s;",
					TABLE_NAME, VIEW, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Playlist playlist = fetchResultSet(db.resultSet);
				list.add(playlist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Playlist> getLatestPlaylists(int top) {
		List<Playlist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC LIMIT 0, %s;",
					TABLE_NAME, CREATED, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Playlist playlist = fetchResultSet(db.resultSet);
				list.add(playlist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	private void setParameters(PreparedStatement prep, Playlist playlist) throws SQLException {
		prep.setNString(1, playlist.getName());
		prep.setNString(2, playlist.getDescription());
		prep.setInt(3, playlist.getAuthorId());
	}
	
	private Playlist fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String name = rs.getNString(NAME);
		String description = rs.getNString(DESCRIPTION);
		Date created = rs.getDate(CREATED);
		int authorId = rs.getInt(AUTHOR);
		long view = rs.getLong(VIEW);
		String photo = rs.getString(PHOTO);
		Playlist playlist = new Playlist(id, name, description, created, authorId, view, photo)
;		return playlist;
	}
}
