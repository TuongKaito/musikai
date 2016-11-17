package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.User;

public interface IUserDAO {
	//Table name of users database
	public static final String TABLE_NAME = "users";
	
	//All columns of table user
	public static final String ID = "id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String DATE_OF_BIRTH = "date_of_birth";
	public static final String GENDER = "gender";
	public static final String EMAIL = "email";
	public static final String CITY = "city";
	public static final String PHOTO = "photo";
	
	public List<User> getAllUsers();
	public User getUser(int id);
	public User addUser(User user);
	public boolean updateUser(User user);
	public boolean deleteUser(int id);
	public List<User> searchUsersByName(String name);
	
}
