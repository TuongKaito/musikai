package com.kaito.musiconline.model;

public enum CommentType {

	SONG("cmt_sg"),
	ALBUM("cmt_al"),
	PLAYLIST("cmt_pl"),
	NONE("none");
	
	private final String type;
	
	private CommentType(final String type) {
		this.type = type;
	}
	
	public static CommentType getType(String name) {
		for(CommentType cmtType : CommentType.values()) {
			if (name.equalsIgnoreCase(cmtType.name()) || name.equals(cmtType.toString())) {
				return cmtType;
			}
		}
		return NONE;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
}
