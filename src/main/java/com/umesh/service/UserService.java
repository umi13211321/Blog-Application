package com.umesh.service;

import com.umesh.entity.User;
import com.umesh.users.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);

    void save(UserDetails userDetails);

    List<String> getAllAuthors();
}
