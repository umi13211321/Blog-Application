package com.umesh.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.umesh.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.umesh.entity.Comment;
import com.umesh.entity.Post;
import com.umesh.entity.Tag;


@Repository
public class PostDaoImplementation implements PostDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Post> getPosts() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Post> query = currentSession.createQuery("from Post", Post.class);
        List<Post> listOfPosts = query.getResultList();
        return listOfPosts;
    }

    @Override
    public void saveStory(int theId, Post thePost) {
        System.out.println("inside save story dao");
        Session currentSession = sessionFactory.getCurrentSession();
        if (theId == 0) {
            currentSession.save(thePost);
            System.out.println("story saved");
        } else {
            Post post = currentSession.get(Post.class, theId);
            post.setTitle(thePost.getTitle());
            post.setContent(thePost.getContent());
            post.setAuthor(thePost.getAuthor());
        }

        String author = thePost.getAuthor();
        System.out.println(author);

        Query theQuery = null;
        theQuery = currentSession.createQuery("from User where name like :author", User.class);
        theQuery.setParameter("author", author);
        List<User> user = theQuery.getResultList();
        thePost.setUser(user.get(0));
        System.out.println(user.get(0));
    }

    public void saveStory(Post thePost) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(thePost);

        String author = thePost.getAuthor();
        Query theQuery = null;
        theQuery = currentSession.createQuery("from User where name like :author", User.class);
        theQuery.setParameter("author", author);
        List<User> user = theQuery.getResultList();
        thePost.setUser(user.get(0));
        System.out.println(user.get(0));
    }

    @Override
    public Post getPosts(int theId) {
        System.out.println("inside get post dao");
        Session currentSession = sessionFactory.getCurrentSession();
        Post thePost = currentSession.get(Post.class, theId);
        return thePost;
    }

    @Override
    public void deleteStory(int theId) {
        System.out.println("inside delete story dao");
        Session currentSession = sessionFactory.getCurrentSession();
        Post thePost = currentSession.get(Post.class, theId);
        currentSession.remove(thePost);
    }

    @Override
    public void saveComment(int theId, Comment theComment) {
        System.out.println("inside save comment dao");
        Session currentSession = sessionFactory.getCurrentSession();
        Post thePost = currentSession.get(Post.class, theId);
        theComment.setPost(thePost);
        thePost.getComment().add(theComment);
        currentSession.saveOrUpdate(thePost);

    }

    @Override
    public Comment getComment(int theId) {
        System.out.println("inside get comment dao");
        Session currentSession = sessionFactory.getCurrentSession();
        Comment theComment = currentSession.get(Comment.class, theId);
        return theComment;
    }

    @Override
    public void deleteComment(int storyId, int commentId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Comment theComment = currentSession.get(Comment.class, commentId);

        Post thePost = currentSession.get(Post.class, storyId);
        thePost.getComment().remove(theComment);
        System.out.println("inside delete comment dao");
        System.out.println(theComment);
        currentSession.remove(theComment);
    }

    @Override
    public void saveUpdatedComment(int storyId, int commentId, Comment theComment) {
        Session currentSession = sessionFactory.getCurrentSession();

        Comment comment = currentSession.get(Comment.class, commentId);
        comment.setName(theComment.getName());
        comment.setEmail(theComment.getEmail());
        comment.setComment(theComment.getComment());
    }

    @Override
    public List<Post> searchPosts(String theSearchName) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = null;
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            query = currentSession.createQuery("from Post where lower(author) like :theName or lower(title) like :theName or content like :theName");
            query.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        } else {
            query = currentSession.createQuery("from Post", Post.class);
        }
        List<Post> listOfPosts = query.getResultList();
        return listOfPosts;
    }

    @Override
    public List<Post> getListOfPosts(int theSortField) {
        Session currentSession = sessionFactory.getCurrentSession();
        String query = "";

        if (theSortField == 1) {
            query += "from Post order by publishedAt ASC";
        } else {
            query += "from Post order by publishedAt DESC";
        }
        Query<Post> theQuery = currentSession.createQuery(query, Post.class);
        List<Post> thePost = theQuery.getResultList();
        return thePost;
    }

    @Override
    public void saveTag(int storyId, String theTag) {
        Session currentSession = sessionFactory.getCurrentSession();
        Post thePost = currentSession.get(Post.class, storyId);

        String queryStringForTags = "from Tag";
        Query<Tag> queryTag = currentSession.createQuery(queryStringForTags, Tag.class);
        List<Tag> allTags = queryTag.getResultList();
        String[] listOfTags = theTag.split(",");
        List<String> alreadyPresentTags = new ArrayList<>();

        if (allTags.isEmpty()) {
            for (String str : listOfTags) {
                Tag tag = new Tag();
                tag.setName(str);
                tag.addPosts(thePost);
                currentSession.save(tag);
            }
        } else {
            List<String> allTagName = new ArrayList<>();

            for (Tag tag : allTags) {
                allTagName.add(tag.getName());
            }

            for (String str : listOfTags) {
                if (allTagName.contains(str)) {
                    alreadyPresentTags.add(str);
                } else {
                    Tag tag = new Tag();
                    tag.setName(str);
                    tag.addPosts(thePost);
                    currentSession.save(tag);
                }
            }
        }

        for (Tag tag : allTags) {
            if (alreadyPresentTags.contains(tag.getName())) {
                tag.addPosts(thePost);
            }
        }
    }

    @Override
    public List<Tag> getTags() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Tag> query = currentSession.createQuery("from Tag", Tag.class);
        List<Tag> listOfTags = query.getResultList();
        return listOfTags;
    }

    @Override
    public List<Integer> getFilteredTagData(String tag) {
        List<Integer> tagId = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("SELECT post_id from post_tags where tag_id in (SELECT id From tag where name = :tag)");
        query.setParameter("tag", tag);
        tagId = query.getResultList();
        System.out.println("getFilteredTagData dao " + tagId);
        return tagId;
    }

    @Override
    public List<Integer> getFilteredAuthorData(String author) {
        List<Integer> authorId = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("SELECT id from post where author = :author");
        query.setParameter("author", author);
        authorId = query.getResultList();
        return authorId;
    }

    @Override
    public List<Post> getPostsOffset(int pageNo) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Post> query = currentSession.createQuery("from Post", Post.class);
        List<Post> listOfPosts = query.setFirstResult(pageNo).setMaxResults(5).getResultList();
        return listOfPosts;
    }

    @Override
    public boolean getUsernameAlreadyExist(String userName) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<User> query = currentSession.createQuery("from User", User.class);
        List<User> listOfUsers = query.getResultList();
        for (User user : listOfUsers) {
            if (user.getUsername().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveUser(User theUser) {
        System.out.println("inside save User dao");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(theUser);
    }

    @Override
    public void updateCommentREST(String name, String comment, String email, int commentId) {
        Session currentSession = sessionFactory.getCurrentSession();

        Comment theComment = currentSession.get(Comment.class, commentId);
        theComment.setName(name);
        theComment.setEmail(email);
        theComment.setComment(comment);
    }

    @Override
    public List<String> getAuthors() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<User> query = currentSession.createQuery("from User", User.class);
        List<User> listOfUsers = query.getResultList();

        List<String> listOfAuthors = new ArrayList<>();
        for(User user:listOfUsers){
            listOfAuthors.add(user.getName());
        }
        return listOfAuthors;
    }
}