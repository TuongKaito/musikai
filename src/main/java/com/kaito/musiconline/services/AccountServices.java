package com.kaito.musiconline.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.kaito.musiconline.daoimp.AccountDAO;
import com.kaito.musiconline.daoimp.UserDAO;
import com.kaito.musiconline.exceptions.AccountNotFoundException;
import com.kaito.musiconline.exceptions.PasswordNotMatchException;
import com.kaito.musiconline.exceptions.ServicesException;
import com.kaito.musiconline.model.Account;
import com.kaito.musiconline.model.User;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountServices {

	AccountDAO accountDao = new AccountDAO();
	
	@GET
	public List<Account> getAllAccounts() {
		List<Account> listOfAccounts = accountDao.getAllAccounts();
		return listOfAccounts;
	}
	
	@GET	
	@Path("/{username}")
	public Response getAccount(@PathParam("username") String username) {
		Account account = accountDao.getAccount(username);
		if (account == null) {
			throw new AccountNotFoundException("Account " + username + " is not found");
		}
		return Response.ok(account).build();
	}
	
	@POST
	public Response addAccount(Account account) {
		if (!accountDao.addAccount(account)) {
			throw new ServicesException();
		}
		return Response.status(Status.CREATED).entity(account).build();
	}
	
	@POST
	@Path("/register")
	public Response register(Account account, User user) {
		if (accountDao.register(account, user)) {
			return Response.status(Status.CREATED).build();
		}
		else {
			throw new ServicesException();
		}
	}
	
	@PUT
	@Path("/login")
	public Response login(Account account) {
		User user = null;
		int userId = accountDao.login(account);
		if (userId != 0) {
			user = new UserDAO().getUser(userId);
			return Response.ok(user).build();
		}
		return Response.status(Status.FORBIDDEN).build();
	}
	
	@PUT
	@Path("/{username}") 
	public Response changePassword(Account account, @PathParam("username") String username) {
		if (account == null) {
			throw new AccountNotFoundException("This account does not exist");
		}
		if (!username.equalsIgnoreCase(account.getUsername())) {
			throw new PasswordNotMatchException("Password is not matched");
		}
		else {
			accountDao.changePassword(account);
			return Response.ok().build();
		}
	}
	
	@DELETE
	@Path("/{username}")
	public void deleteAccount(@PathParam("username") String username) {
		accountDao.deleteAccount(username);
	}
}
