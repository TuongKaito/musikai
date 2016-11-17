package com.kaito.musiconline.model;

import java.util.Date;

public class Album {

	private int id;
	private String name;
	private String description;
	private int artistId;
	private Date created;
	private long view;
	private String photo;
	
	public Album() {
		super();
	}

	public Album(int id, String name, String description, int artistId, Date created, long view, String photo) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.artistId = artistId;
		this.created = created;
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

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
