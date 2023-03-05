package com.umesh.service;

import java.util.List;

import com.umesh.entity.User;
import org.springframework.ui.Model;

import com.umesh.entity.Comment;
import com.umesh.entity.Post;
import com.umesh.entity.Tag;


public interface PostService {

    public List<Post> getPostData();

    public void saveStory(int theId, Post thePost);

    public void saveStory(Post thePost);

    public Post getPostData(int theId);

    public void updatePost(int theId, Model theModel);

    public Post getPost(int theId);

    public void deleteStory(int theId);

    public void saveComment(int theId, Comment theComment);

    public Comment getComment(int theId);

    public void deleteComment(int storyId, int commentId);

    public void saveUpdatedComment(int parseInt, int parseInt2, Comment theComment);

    public List<Post> searchPosts(String theSearchName);

    public List<Post> getListOfPosts(int theSortField);

    public void saveTag(int storyId, String theTag);

    public List<Tag> getTags();

    public List<Post> getFilteredPostData(String tag, String author);

    public List<Post> getPostsOffset(int pageNo);

    public boolean getUsernameAlreadyExist(String userName);

    public void saveuser(User theUser);

    public void updateCommentREST(String name, String comment, String email, int commentId);

    public List<String> getAuthors();
}
