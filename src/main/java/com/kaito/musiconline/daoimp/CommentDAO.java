package com.kaito.musiconline.daoimp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kaito.musiconline.dao.ICommentDAO;
import com.kaito.musiconline.dao.ICommentType;
import com.kaito.musiconline.database.DBService;
import com.kaito.musiconline.model.Comment;
import com.kaito.musiconline.model.CommentType;

public class CommentDAO implements ICommentDAO, ICommentType {

	DBService db;
	private String selectStatement;
	
	public CommentDAO() {
		db = new DBService();
		selectStatement = String.format("SELECT %1$s.%2$s, %3$s, %4$s as %5$s,"
					+ " %6$s, %7$s, %8$s, %1$s.%9$s"
					+ " FROM %1$s JOIN %10$s ON %1$s.%5$s = %10$s.%11$s",
					ICommentDAO.TABLE_NAME, ICommentDAO.ID, ICommentDAO.COMMENT, ICommentType.TYPE,
					ICommentDAO.COMMENT_TYPE, ICommentDAO.CREATE_AT, ICommentDAO.TYPE_ID, ICommentDAO.USER_ID,
					ICommentDAO.LIKE, ICommentType.TABLE_NAME, ICommentType.ID);
	}
	
	public List<Comment> getAllComments() {
		List<Comment> list = new ArrayList<>();
		try {
			db.open();
			String query = selectStatement;
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Comment comment = fetchResultSet(db.resultSet);
				list.add(comment);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;		
	}
	
	public Comment getComment(int id) {
		Comment comment = null;
		try {
			db.open();
			String query = String.format("%s WHERE %s.%s = ?;", selectStatement, ICommentDAO.TABLE_NAME,
					ICommentDAO.ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				comment = fetchResultSet(db.resultSet);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return comment;
	}
	
	public Comment addComment(Comment comment) {
		Comment newComment = null;
		try {
			db.open();
			//Query has 3 default values id, date and like
			String query = String.format("INSERT INTO %s VALUES(default, ?, ?, default, ?, ?, default);",
					ICommentDAO.TABLE_NAME);
			db.prepStatement = db.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			setParameters(db.prepStatement, comment);
			db.prepStatement.executeUpdate();
			db.resultSet = db.prepStatement.getGeneratedKeys();
			db.resultSet.next();
			int insertedId = db.resultSet.getInt(1);
			newComment = getComment(insertedId);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return newComment;
	}
	
	public boolean updateComment(Comment comment) {
		boolean success = false;
		try {
			db.open();
			String query = String.format("UPDATE %s SET %s=?, %s=? "
					+ "WHERE %s=?", ICommentDAO.TABLE_NAME, COMMENT, LIKE, ICommentType.ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setNString(1, comment.getComment());
			db.prepStatement.setLong(2, comment.getLike());
			db.prepStatement.setInt(3, comment.getId());
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected > 0)
				success = true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public boolean deleteComment(int id) {
		boolean success = false;
		try {
			db.open();
			
			//Then delete comment
			String query = String.format("DELETE FROM %s WHERE %s = ?;", ICommentDAO.TABLE_NAME,
					ICommentDAO.ID);
			db.prepStatement = db.connection.prepareStatement(query);
			db.prepStatement.setInt(1, id);
			int rowAffected = db.prepStatement.executeUpdate();
			if (rowAffected > 0)
				success = true;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return success;
	}
	
	public List<Comment> getTopComments(int size) {
		List<Comment> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("%s GROUP BY %s DESC LIMIT 0, %s;", selectStatement,
					ICommentDAO.LIKE, size);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Comment comment = fetchResultSet(db.resultSet);
				list.add(comment);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	public List<Comment> getTopComments(int size, CommentType type) {
		List<Comment> list = new ArrayList<>();
		if (!type.equals(CommentType.NONE)) {
			try {
				db.open();
				String query = String.format("%s WHERE %s.%s = ? GROUP BY %s DESC LIMIT 0, %s;", selectStatement,
						ICommentDAO.TABLE_NAME, ICommentDAO.COMMENT_TYPE, ICommentDAO.LIKE, size);
				db.prepStatement = db.connection.prepareStatement(query);
				db.prepStatement.setString(1, type.toString());
				db.resultSet = db.prepStatement.executeQuery();
				while (db.resultSet.next()) {
					Comment comment = fetchResultSet(db.resultSet);
					list.add(comment);
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
		return list;
	}
	
	public List<Comment> getLatestComments(int size) {
		List<Comment> list = new ArrayList<>();
		try {
			db.open();
			String query = String.format("%s GROUP BY %s DESC LIMIT 0, %s;", selectStatement,
					ICommentDAO.CREATE_AT, size);
			db.prepStatement = db.connection.prepareStatement(query);
			db.resultSet = db.prepStatement.executeQuery();
			while (db.resultSet.next()) {
				Comment comment = fetchResultSet(db.resultSet);
				list.add(comment);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return list;
	}
	
	@Override
	public List<Comment> getLastestComments(int size, CommentType type) {
		List<Comment> list = new ArrayList<>();
		if (!type.equals(CommentType.NONE)) {
			try {
				db.open();
				String query = String.format("%s WHERE %s.%s = ? GROUP BY %s DESC LIMIT 0, %s;", selectStatement,
						ICommentDAO.TABLE_NAME, ICommentDAO.COMMENT_TYPE, ICommentDAO.CREATE_AT, size);
				db.prepStatement = db.connection.prepareStatement(query);
				db.prepStatement.setString(1, type.toString());
				db.resultSet = db.prepStatement.executeQuery();
				while (db.resultSet.next()) {
					Comment comment = fetchResultSet(db.resultSet);
					list.add(comment);
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
		return list;
	}
	
	@Override
	public List<Comment> getCommentsOfType(int id, CommentType type) {
		List<Comment> listOfComments = new ArrayList<Comment>();
		if (!type.equals(CommentType.NONE)) {
			try {
				db.open();
				String query = String.format("%1$s WHERE %2$s.%3$s = ? AND %2$s.%4$s = ? ORDER BY %2$s.%5$s DESC;", selectStatement,
						ICommentDAO.TABLE_NAME, ICommentDAO.TYPE_ID, ICommentDAO.COMMENT_TYPE, ICommentDAO.CREATE_AT);
				db.prepStatement = db.connection.prepareStatement(query);
				db.prepStatement.setInt(1, id);
				db.prepStatement.setString(2, type.toString());
				db.resultSet = db.prepStatement.executeQuery();
				while (db.resultSet.next()) {
					Comment comment = fetchResultSet(db.resultSet);
					listOfComments.add(comment);
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
		return listOfComments;
	}

	@Override
	public List<Comment> getCommentsOfType(CommentType type) {
		List<Comment> list = new ArrayList<>();
		if (!type.equals(CommentType.NONE)) {
			try {
				db.open();
				String query = String.format("%s WHERE %s.%s = ?;", selectStatement,
						ICommentDAO.TABLE_NAME, ICommentDAO.COMMENT_TYPE);
				db.prepStatement = db.connection.prepareStatement(query);
				db.prepStatement.setString(1, type.toString());
				db.resultSet = db.prepStatement.executeQuery();
				while (db.resultSet.next()) {
					Comment comment = fetchResultSet(db.resultSet);
					list.add(comment);
				}
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				db.close();
			}
		}
		return list;
	}
	
	private void setParameters(PreparedStatement prep, Comment comment) throws SQLException {
		prep.setNString(1, comment.getComment());
		CommentType type = CommentType.getType(comment.getCommentType());
		prep.setString(2, type.toString());
		prep.setInt(3, comment.getTypeId());
		prep.setInt(4, comment.getUserId());
	}
	
	private Comment fetchResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(ICommentDAO.ID);
		String cmt = rs.getNString(COMMENT);
		CommentType type = CommentType.getType(rs.getString(COMMENT_TYPE));
		String cmtType = rs.getString(type.toString());
		Date date = rs.getDate(CREATE_AT);
		int typeId = rs.getInt(TYPE_ID);
		int userId = rs.getInt(USER_ID);
		long like = rs.getLong(LIKE);
		Comment comment = new Comment(id, cmt, cmtType, date, typeId, userId, like);
		return comment;
	}
}
