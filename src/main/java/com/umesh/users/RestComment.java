package com.umesh.users;

public class RestComment {

    private String name;

    private String comment;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RestComments{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
