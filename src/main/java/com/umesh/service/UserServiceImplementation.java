package com.umesh.service;

import com.umesh.dao.RoleDao;
import com.umesh.dao.UserDao;
import com.umesh.entity.Role;
import com.umesh.entity.User;
import com.umesh.users.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    @Transactional
    public void save(UserDetails userDetails) {
        User user = new User();

        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        user.setUsername(userDetails.getUsername());
        user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_Author")));
        System.out.println("Inside save user " + roleDao.findRoleByName("ROLE_Author"));

        userDao.save(user);
    }

    @Override
    @Transactional
    public List<String> getAllAuthors() {
        List<String> authors = userDao.getAllAuthors();
        return authors;
    }

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthorities(user.getRoles()));
    }

    @Transactional
    private static List<GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
