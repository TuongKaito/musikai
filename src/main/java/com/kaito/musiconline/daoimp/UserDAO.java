package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.IUserDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.User;

public class UserDAO implements IUserDAO {

	DBService db;
	
	public UserDAO() {
		db = new DBService();
	}
	
	public List<User> getAllUsers() {
		List<User> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				User user = fetchResultSet(db.resultSet);
				list.add(user);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public User getUser(int id) {
		User user = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				user = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return user;
	}
	
	public User addUser(User user) {
		User newUser = null;
		try {
			db.open();
			//Query has 2 default values id and photo
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, ?, ?, ?, ?, default);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, user);
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected <= 0) {
				throw new SQLException();
			}
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			newUser = getUser(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return newUser;
	}
	
	public User addUser(User user, DBService dbs) throws SQLException {
		User newUser = null;
		if (dbs == null) {
			newUser = addUser(user);
		}
		else {
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, ?, ?, ?, ?, default);", TABLE_NAME);
			dbs.prepStatement = dbs.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(dbs.prepStatement, user);
			dbs.prepStatement.executeUpdate();
			dbs.resultSet = dbs.prepStatement.getGeneratedKeys();
			dbs.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			newUser = getUser(insertedId);
		}
		return newUser;
	}
	
	public boolean updateUser(User user) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, "
					+ "%s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, FIRST_NAME,
					LAST_NAME, DATE_OF_BIRTH, GENDER, EMAIL, CITY, PHOTO, ID);
			db.prepStatement = db.connection.prepareStatement(query);
			//Set 6 parameters for prepared statement except id and photo
			setParameters(db.prepStatement, user);
			//Set photo parameter at index 7 and id parameter at index 8
			db.prepStatement.setString(7, user.getPhoto());
			db.prepStatement.setInt(8, user.getId());
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected > 0) 
				success= true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public boolean deleteUser(int id) {
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
	
	public List<User> searchUsersByName(String name) {
		List<User> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s like ? OR %s like ?;", 
					TABLE_NAME, FIRST_NAME, LAST_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, "%" + name + "%");
			db.prepStatement.setNString(2, "%" + name + "%");
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				User user = fetchResultSet(db.resultSet);
				list.add(user);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;	
	}
	
	private void setParameters(PreparedStatement prep, User user) throws SQLException {
		prep.setNString(1, user.getFirstName());
		prep.setNString(2, user.getLastName());
		prep.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
		prep.setNString(4, user.getGender());
		prep.setString(5, user.getEmail());
		prep.setNString(6, user.getCity());
	}
	
	private User fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String firstName = rs.getNString(FIRST_NAME);
		String lastName = rs.getNString(LAST_NAME);
		Date dateOfBirth = rs.getDate(DATE_OF_BIRTH);
		String gender = rs.getNString(GENDER);
		String email = rs.getString(EMAIL);
		String city = rs.getNString(CITY);
		String photo = rs.getString(PHOTO);
		User user = new User(id, firstName, lastName, dateOfBirth, gender, email, city, photo);
		return user;
	}
}
