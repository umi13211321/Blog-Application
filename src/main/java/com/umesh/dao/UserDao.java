package com.umesh.dao;

import com.umesh.entity.User;

import java.util.List;

public interface UserDao {
    User findByUserName(String userName);

    void save(User user);

    List<String> getAllAuthors();
}
