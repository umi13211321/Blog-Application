package com.umesh.dao;

import java.util.List;

import com.umesh.entity.User;
import org.springframework.ui.Model;

import com.umesh.entity.Comment;
import com.umesh.entity.Post;
import com.umesh.entity.Tag;

public interface PostDao {

    public List<Post> getPosts();

    public void saveStory(int theId, Post thePost);

    public void saveStory(Post thePost);

    public Post getPosts(int theId);

    public void deleteStory(int theId);

    public void saveComment(int theId, Comment theComment);

    public Comment getComment(int theId);

    public void deleteComment(int storyId, int commentId);

    public void saveUpdatedComment(int storyId, int commentId, Comment theComment);

    public List<Post> searchPosts(String theSearchName);

    public List<Post> getListOfPosts(int theSortField);

    public void saveTag(int storyId, String theTag);

    public List<Tag> getTags();

    public List<Integer> getFilteredTagData(String str);

    public List<Integer> getFilteredAuthorData(String str);

    public List<Post> getPostsOffset(int pageNo);

    public boolean getUsernameAlreadyExist(String userName);

    public void saveUser(User theUser);

    public void updateCommentREST(String name, String comment, String email, int commentId);

    public List<String> getAuthors();
}

