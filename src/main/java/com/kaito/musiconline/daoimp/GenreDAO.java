package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kaito.musiconline.dao.IGenreDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Genre;

public class GenreDAO implements IGenreDAO {

	DBService db;
	
	public GenreDAO() {
		db = new DBService();
	}
	
	public List<Genre> getAllGenres() {
		List<Genre> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Genre genre = fetchResultSet(db.resultSet);
				list.add(genre);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Genre getGenre(int id) {
		Genre genre = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				genre = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return genre;
	}
	
	public Genre addGenre(Genre genre) {
		try {
			db.open();
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, genre);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			genre.setId(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return genre;
	}
	
	public boolean updateGenre(Genre genre) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=? WHERE %s=?",
					TABLE_NAME, NAME, DESCRIPTION, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 2 parameters for prepared statement except id
			setParameters(db.prepStatement, genre);
			//Set id parameter at index 3
			db.prepStatement.setInt(3, genre.getId());
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
	
	public boolean deleteGenre(int id) {
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
	
	private void setParameters(PreparedStatement prep, Genre genre) throws SQLException {
		prep.setNString(1, genre.getName());
		prep.setNString(2, genre.getDescription());
	}
	
	private Genre fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String name = rs.getNString(NAME);
		String description = rs.getNString(DESCRIPTION);
		Genre genre = new Genre(id, name ,description);
		return genre;
	}
}
