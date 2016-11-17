package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kaito.musiconline.dao.IAccountDAO;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Account;
import com.kaito.musiconline.model.User;

public class AccountDAO implements IAccountDAO {

	DBService db;
	
	public AccountDAO() {
		db = new DBService();
	}
	
	//Return user id if login success
	public int login(Account account) {
		int userId = 0;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?;", TABLE_NAME,
					USERNAME, PASSWORD);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setString(1, account.getUsername());
			db.prepStatement.setString(2, account.getPassword());
			db.resultSet = db.prepStatement.executeQuery();
			if (db.resultSet.next())
				userId = db.resultSet.getInt(USER_ID);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return userId;
	}
	
	public List<Account> getAllAccounts() {
		List<Account> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("SELECT * FROM %s;", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Account account = fetchResultSet(db.resultSet);
				list.add(account);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Account getAccount(String username) {
		Account account = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, USERNAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setString(1, username);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				account = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return account;
	}
	
	public Account getAccountByUserId(int userId) {
		Account account = null;
		try {
			db.open();
			String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE_NAME, USER_ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, userId);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				account = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return account;
	}
	
	public boolean addAccount(Account account) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("INSERT INTO %s VALUES(?, ?, ?);", TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, account);
			db.prepStatement.executeUpdate();
			success = true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public boolean register(Account account, User user) {
		boolean success = false;
		DBService dbs = new DBService();
		try {
			UserDAO userDao = new UserDAO();
			dbs.connection.setAutoCommit(false);
			dbs.open();
			user = userDao.addUser(user, dbs);
			if (user == null) {
				throw new SQLException();
			}
			account.setUserId(user.getId());
			String query = String.format("INSERT INTO %s VALUES(?, ?, ?);", TABLE_NAME);
			dbs.prepStatement = dbs.connection.prepareStatement(query);
			setParameters(dbs.prepStatement, account);
			if (dbs.prepStatement.executeUpdate() <= 0) {
				throw new SQLException();
			}
			success = true;
			dbs.connection.commit();
		} catch(SQLException | ClassNotFoundException e) {
			try {
				if (dbs.connection != null)
					dbs.connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			dbs.close();
		}
		return success;
	}
	
	public boolean changePassword(Account account) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=? WHERE %s=?",
					TABLE_NAME, PASSWORD, USERNAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setString(1, account.getPassword());
			db.prepStatement.setString(2, account.getUsername());
			if (db.prepStatement.executeUpdate() > 0)
				success = true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public void deleteAccount(String username) {
		try {
			db.open();
			//Delete account of user
			String query = String.format("DELETE FROM %s WHERE %s = ?;", TABLE_NAME, USERNAME);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setString(1, username);
			db.prepStatement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	private void setParameters(PreparedStatement prep, Account account) throws SQLException {
		prep.setString(1, account.getUsername());
		prep.setString(2, account.getPassword());
		prep.setInt(3, account.getUserId());
	}
	
	private Account fetchResultSet(ResultSet rs) throws SQLException {
		String username = rs.getString(USERNAME);
		String password = rs.getString(PASSWORD);
		int userId = rs.getInt(USER_ID);
		Account account = new Account(username, password ,userId);
		return account;
	}	
	
}
