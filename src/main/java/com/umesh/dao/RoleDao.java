package com.umesh.dao;

import com.umesh.entity.Role;

public interface RoleDao {
    Role findRoleByName(String roleAuthor);
}

