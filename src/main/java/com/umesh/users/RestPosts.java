package com.umesh.users;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class RestPosts {
    private String title;
    private String content;
    private String author;
    private String tags;

    public RestPosts() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "RestPosts{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
