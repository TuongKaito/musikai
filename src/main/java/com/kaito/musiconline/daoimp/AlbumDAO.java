package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.IAlbumDAO;
import com.kaito.musiconline.dao.IAlbumSongs;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Album;

public class AlbumDAO implements IAlbumDAO {

	DBService db;
	
	public AlbumDAO() {
		db = new DBService();
	}
	
	public List<Album> getAllAlbums() {
		List<Album> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Album album = fetchResultSet(db.resultSet);
				list.add(album);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Album getAlbum(int id) {
		Album album = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				album = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return album;
	}
	
	public Album addAlbum(Album album) {
		try {
			db.open();
			//Query has 3 default values id, view and photo
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, ?, ?, default, default);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, album);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			album.setId(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return album;
	}
	
	public boolean updateAlbum(Album album) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? "
					+ "WHERE %s=?", TABLE_NAME, NAME, DESCRIPTION, ARTIST, CREATED,
					VIEW, PHOTO, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 4 parameters for prepared statement except id, view and photo
			setParameters(db.prepStatement, album);
			//Set view parameter at index 5, photo parameter at index 6 and id parameter at index 7
			db.prepStatement.setLong(5, album.getView());
			db.prepStatement.setString(6, album.getPhoto());
			db.prepStatement.setInt(7, album.getId());
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
	
	public boolean deleteAlbum(int id) {
		boolean success = false;
		try {
			db.open();
			//Delete all songs in album first
			String query = String.format("DELETE FROM %s WHERE %s = ?;", IAlbumSongs.TABLE_NAME,
					IAlbumSongs.ALBUM_ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.prepStatement.executeUpdate();
			
			//Then delete album
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
	
	public List<Album> searchAlbumsByName(String name) {
		List<Album> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s like ?;", 
					TABLE_NAME, NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, "%" + name + "%");
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Album album = fetchResultSet(db.resultSet);
				list.add(album);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Album> searchAlbumsByArtist(int artistId) {
		List<Album> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s=?;", 
					TABLE_NAME, ARTIST);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, artistId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Album album = fetchResultSet(db.resultSet);
				list.add(album);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Album> getTopAlbums(int top) {
		List<Album> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC LIMIT 0, %s;",
					TABLE_NAME, VIEW, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Album album = fetchResultSet(db.resultSet);
				list.add(album);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Album> getLatestAlbums(int top) {
		List<Album> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC LIMIT 0, %s;",
					TABLE_NAME, CREATED, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Album album = fetchResultSet(db.resultSet);
				list.add(album);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	private void setParameters(PreparedStatement prep, Album album) throws SQLException {
		prep.setNString(1, album.getName());
		prep.setNString(2, album.getDescription());
		prep.setInt(3, album.getArtistId());
		prep.setDate(4, new java.sql.Date(album.getCreated().getTime()));
	}
	
	private Album fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String name = rs.getNString(NAME);
		String description = rs.getNString(DESCRIPTION);
		int artistId = rs.getInt(ARTIST);
		Date created = rs.getDate(CREATED);
		long view = rs.getLong(VIEW);
		String photo = rs.getString(PHOTO);
		Album album = new Album(id, name, description, artistId, created, view, photo);
		return album;
	}
}
