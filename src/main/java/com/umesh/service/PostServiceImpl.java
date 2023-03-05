package com.umesh.service;

import java.util.*;

import javax.transaction.Transactional;

import com.umesh.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.umesh.dao.PostDao;
import com.umesh.entity.Comment;
import com.umesh.entity.Post;
import com.umesh.entity.Tag;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDao postDao;

    @Override
    @Transactional
    public List<Post> getPostData() {
        return postDao.getPosts();
    }

    @Override
    @Transactional
    public void saveStory(int theId, Post thePost) {
        System.out.println("inside save story service");
        postDao.saveStory(theId, thePost);
    }

    @Override
    @Transactional
    public void saveStory(Post thePost) {
        postDao.saveStory(thePost);
    }

    @Override
    @Transactional
    public Post getPostData(int theId) {
        return postDao.getPosts(theId);
    }

    @Override
    @Transactional
    public void updatePost(int theId, Model theModel) {
        System.out.println("inside updatePost Service");
    }

    @Override
    @Transactional
    public Post getPost(int theId) {
        System.out.println("inside getPost service");
        return postDao.getPosts(theId);
    }

    @Override
    @Transactional
    public void deleteStory(int theId) {
        Post post = postDao.getPosts(theId);
        post.setTags(null);
        post.setUser(null);
        postDao.deleteStory(theId);
    }

    @Override
    @Transactional
    public void saveComment(int theId, Comment theComment) {
        System.out.println("inside saveComment Service");
        postDao.saveComment(theId, theComment);
    }

    @Override
    @Transactional
    public Comment getComment(int theId) {
        return postDao.getComment(theId);
    }

    @Override
    @Transactional
    public void deleteComment(int storyId, int commentId) {
        postDao.deleteComment(storyId, commentId);
    }

    @Override
    @Transactional
    public void saveUpdatedComment(int storyId, int commentId, Comment theComment) {
        postDao.saveUpdatedComment(storyId, commentId, theComment);
    }

    @Transactional
    @Override
    public List<Post> searchPosts(String theSearchName) {
        return postDao.searchPosts(theSearchName);
    }

    @Override
    @Transactional
    public List<Post> getListOfPosts(int theSortField) {
        return postDao.getListOfPosts(theSortField);
    }

    @Override
    @Transactional
    public void saveTag(int storyId, String theTag) {
        postDao.saveTag(storyId, theTag);
    }

    @Override
    @Transactional
    public List<Tag> getTags() {
        return postDao.getTags();
    }

    @Override
    @Transactional
    public List<Post> getFilteredPostData(String tag, String author) {
        System.out.println("author:" + author);
        System.out.println("tag: " + tag);
        List<Post> post = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        String[] arr1 = tag.split(",");//[racing, car]
        String[] arr2 = author.split(",");

        if (!arr1[0].equals("xyzxyzxyz")) {
            for (String str : arr1) {
                ArrayList<Integer> list = new ArrayList<>();
                list.addAll(postDao.getFilteredTagData(str));
                set.addAll(list);
            }
        }

        if (!arr2[0].equals("xyzxyzxyz")) {
            for (String str : arr2) {
                ArrayList<Integer> list = new ArrayList<>();
                list.addAll(postDao.getFilteredAuthorData(str));
                set.addAll(list);
            }
        }

        for (int n : set) {
            Post thePost = postDao.getPosts(n);
            post.add(thePost);
        }
        return post;
    }

    @Override
    @Transactional
    public List<Post> getPostsOffset(int pageNo) {
        return postDao.getPostsOffset(pageNo);
    }

    @Override
    @Transactional
    public boolean getUsernameAlreadyExist(String userName) {
        return postDao.getUsernameAlreadyExist(userName);
    }

    @Override
    @Transactional
    public void saveuser(User theUser) {
        postDao.saveUser(theUser);
    }

    @Override
    @Transactional
    public void updateCommentREST(String name, String comment, String email, int commentId) {
        postDao.updateCommentREST(name, comment, email, commentId);
    }

    @Override
    @Transactional
    public List<String> getAuthors() {
        Set<String> setAuthor = new HashSet<>();
        setAuthor.addAll(postDao.getAuthors());
        List<String> listOfAuthors = new ArrayList<>(setAuthor);
        return listOfAuthors;
    }
}
