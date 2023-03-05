package com.umesh.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "excerpt")
    private String excerpt;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @CreationTimestamp
    @Column(name = "published_at")
    private Timestamp publishedAt;

    @ColumnDefault("false")
    @Column(name = "is_published")
    private Boolean isPublished;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @JsonManagedReference
    private List<Comment> comment;



    @ManyToMany(fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinTable(
            name="post_tags",
            joinColumns=@JoinColumn(name="post_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Tag> tags;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    @JsonManagedReference
    private User user;



    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTags(Tag theTag) {
        if (tags == null) {
            tags = new ArrayList<>();
            tags.add(theTag);
        }
        tags.add(theTag);
    }

    public void addComment(Comment theComment) {
        if (comment == null) {
            comment = new ArrayList<>();
        }
        comment.add(theComment);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}

