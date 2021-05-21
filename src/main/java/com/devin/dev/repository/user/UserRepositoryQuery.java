package com.devin.dev.repository.user;

import com.devin.dev.entity.user.User;

import java.util.List;

public interface UserRepositoryQuery {

    List<User> findUsers();

    

}
