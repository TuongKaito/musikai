package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Account;
import com.kaito.musiconline.model.User;

public interface IAccountDAO {

	//Table name of accounts database
	public static final String TABLE_NAME = "accounts";
	
	//All columns of table accounts
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String USER_ID = "user_id";
	
	public List<Account> getAllAccounts();
	public Account getAccount(String username);
	public Account getAccountByUserId(int id);
	public boolean addAccount(Account account);
	public boolean register(Account account, User user);
	public void deleteAccount(String username);
	public boolean changePassword(Account account);
	public int login(Account account);
	
}
