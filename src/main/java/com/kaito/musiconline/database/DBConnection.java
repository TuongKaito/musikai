package com.kaito.musiconline.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static final String HOST = "localhost";
	private static final String DATABASE_NAME = "online_music";
	private static final String USER = "kaito";
	private static final String PASSWORD = "tuong";
	
	public DBConnection() {
		
	}
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		//Load the MySQL driver
		Class.forName(MYSQL_DRIVER);
		
		//Connection String of database MySQL
		String connectionString = String.format("jdbc:mysql://%s/%s?useSSL=false&user=%s&password=%s", 
									HOST, DATABASE_NAME, USER, PASSWORD);
		Connection connection = DriverManager.getConnection(connectionString);
		return connection;
	}
}
