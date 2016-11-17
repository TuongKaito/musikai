package com.kaito.musiconline.model;

import java.util.Date;

public class Comment {

	private int id;
	private String comment;
	private String commentType;
	private Date date;
	private int typeId;
	private int userId;
	private long like;
	
	public Comment() {
		super();
	}

	public Comment(int id, String comment, String commentType, Date date, int typeId, int userId, long like) {
		super();
		this.id = id;
		this.comment = comment;
		this.commentType = commentType;
		this.date = date;
		this.userId = userId;
		this.like = like;;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getCommentType() {
		return commentType;
	}
	
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTypeId() {
		return typeId;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getLike() {
		return like;
	}

	public void setLike(long like) {
		this.like = like;
	}
	
}
