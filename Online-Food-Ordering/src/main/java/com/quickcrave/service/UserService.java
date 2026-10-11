package com.quickcrave.service;

import com.quickcrave.model.User;

public interface UserService {

    public User findUserByjwtToken(String jwt) throws Exception;

    public  User findUserByEmail(String email) throws Exception;
}
