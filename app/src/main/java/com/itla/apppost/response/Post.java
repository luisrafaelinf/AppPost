package com.itla.apppost.response;


import java.io.Serializable;
import java.util.Arrays;

public final class Post implements Serializable {

	public static final long serialVersionUID = 1L;

	private int id;
	private int postId;
	private String title;
	private String body;
	private boolean liked;
	private int likes;
	private String userEmail;
	private int userId;
	private String userName;
	private int views;
	private String[] tags;
	private String comments;
	private String createdAt;
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Post{" +
			"id=" + id +
			", title='" + title + '\'' +
			", body='" + body + '\'' +
			", liked=" + liked +
			", likes=" + likes +
			", userEmail='" + userEmail + '\'' +
			", userId=" + userId +
			", userName='" + userName + '\'' +
			", views=" + views +
			", tags=" + Arrays.toString(tags) +
			", comments='" + comments + '\'' +
			'}';
	}
}

