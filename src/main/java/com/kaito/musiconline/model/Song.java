package com.kaito.musiconline.model;

import java.util.Date;

public class Song {

	private int id;
	private String name;
	private int genre;
	private String lyric;
	private String path;
	private int artistId;
	private int authorId;
	private Date uploaded;
	private long view;
	
	public Song() {
		super();
	}

	public Song(int id, String name, int genre, String lyric, String path, int artistId, int authorId, Date uploaded,
			long view) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.lyric = lyric;
		this.path = path;
		this.artistId = artistId;
		this.authorId = authorId;
		this.uploaded = uploaded;
		this.view = view;
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

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public long getView() {
		return view;
	}

	public void setView(long view) {
		this.view = view;
	}
	
}
