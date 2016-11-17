package com.kaito.musiconline.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBService {

	public Connection connection = null;
	public Statement statement = null;
	public PreparedStatement prepStatement = null;
	public ResultSet resultSet = null;
	boolean isOpen = false;
	
	public void open() throws ClassNotFoundException, SQLException {
		connection = DBConnection.getConnection();
		statement = connection.createStatement();
		isOpen = true;
	}
	
	public ResultSet query(String query) {
		ResultSet rs = null;
		try {
			open();
			prepStatement = connection.prepareStatement(query);
			rs = prepStatement.executeQuery();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return rs;
	}
	
	public void close() {
		if (isOpen) {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				statement.close();
				connection.close();
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
