package com.kaito.musiconline.model;

import java.util.Date;

public class Playlist {

	private int id;
	private String name;
	private String description;
	private Date created;
	private int authorId;
	private long view;
	private String photo;
	
	public Playlist() {
		super();
	}

	public Playlist(int id, String name, String description, Date created, int authorId, long view, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.created = created;
		this.authorId = authorId;
		this.view = view;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public long getView() {
		return view;
	}

	public void setView(long view) {
		this.view = view;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
