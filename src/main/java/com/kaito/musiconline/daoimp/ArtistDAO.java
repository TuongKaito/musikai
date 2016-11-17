package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.IArtistDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Artist;

public class ArtistDAO implements IArtistDAO {

	DBService db;
	
	public ArtistDAO() {
		db = new DBService();
	}
	
	public List<Artist> getAllArtists() {
		List<Artist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Artist artist = fetchResultSet(db.resultSet);
				list.add(artist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Artist getArtist(int id) {
		Artist artist = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				artist = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return artist;
	}
	
	public Artist addArtist(Artist artist) {
		try {
			db.open();
			//Query has 2 default values id and like
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, ?, ?, ?, ?, ?, default, ?);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, artist);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			artist = getArtist(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return artist;
	}
	
	public boolean updateArtist(Artist artist) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, "
					+ "%s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, FIRST_NAME,
					LAST_NAME, GENDER, DATE_OF_BIRTH, PLACE_OF_BIRTH, GENRE, PROFILE, PHOTO, LIKE, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 8 parameters for prepared statement except id, like
			setParameters(db.prepStatement, artist);
			//Set like parameter at index 9 and id parameter at index 10
			db.prepStatement.setLong(9, artist.getLike());
			db.prepStatement.setInt(10, artist.getId());
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
	
	public boolean deleteArtist(int id) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("DELETE FROM %s WHERE %s = ?;", TABLE_NAME, ID);
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
	
	public List<Artist> searchArtistsByName(String name) {
		List<Artist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s like ? OR %s like ?;", 
					TABLE_NAME, FIRST_NAME, LAST_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, "%" + name + "%");
			db.prepStatement.setNString(2, "%" + name + "%");
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Artist artist = fetchResultSet(db.resultSet);
				list.add(artist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Artist> searchArtistsByGenre(int genreId) {
		List<Artist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s=?;", 
					TABLE_NAME, GENRE);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, genreId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Artist artist = fetchResultSet(db.resultSet);
				list.add(artist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Artist> getTopFamousArtist(int top) {
		List<Artist> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s GROUP BY %s DESC, %s LIMIT 0, %s;",
					TABLE_NAME, LIKE, ID, top);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Artist artist = fetchResultSet(db.resultSet);
				list.add(artist);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	private void setParameters(PreparedStatement prep, Artist artist) throws SQLException {
		prep.setNString(1, artist.getFirstName());
		prep.setNString(2, artist.getLastName());
		prep.setNString(3, artist.getGender());
		prep.setDate(4, new java.sql.Date(artist.getDateOfBirth().getTime()));
		prep.setNString(5, artist.getPlaceOfBirth());
		prep.setInt(6, artist.getGenreId());
		prep.setNString(7, artist.getProfile());
		prep.setString(8, artist.getPhoto());
	}
	
	private Artist fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String firstName = rs.getNString(FIRST_NAME);
		String lastName = rs.getNString(LAST_NAME);
		String gender = rs.getNString(GENDER);
		Date dateOfBirth = rs.getDate(DATE_OF_BIRTH);
		String placeOfBirth = rs.getNString(PLACE_OF_BIRTH);
		int genre = rs.getInt(GENRE);
		String profile = rs.getNString(PROFILE);
		long like = rs.getLong(LIKE);
		String photo = rs.getString(PHOTO);
		Artist artist = new Artist(id, firstName, lastName, gender, dateOfBirth, placeOfBirth, genre, profile, like, photo);
		return artist;
	}
}
