package com.kaito.musiconline.dao;

import java.util.List;

import com.kaito.musiconline.model.Comment;
import com.kaito.musiconline.model.CommentType;

public interface ICommentDAO {

	//Table name of album database
	public static final String TABLE_NAME = "comments";
	
	//All columns of table albums;
	public static final String ID = "id";
	public static final String COMMENT = "comment";
	public static final String COMMENT_TYPE = "comment_type";
	public static final String CREATE_AT = "create_at";
	public static final String TYPE_ID = "type_id";
	public static final String USER_ID = "user_id";
	public static final String LIKE = "_like";
	
	public List<Comment> getAllComments();
	public Comment getComment(int id);
	public Comment addComment(Comment comment);
	public boolean updateComment(Comment comment);
	public boolean deleteComment(int id);
	public List<Comment> getCommentsOfType(int id, CommentType type);
	public List<Comment> getCommentsOfType(CommentType type);
	public List<Comment> getTopComments(int size);
	public List<Comment> getLatestComments(int size);
	public List<Comment> getTopComments(int size, CommentType type);
	public List<Comment> getLastestComments(int size, CommentType type);
}
