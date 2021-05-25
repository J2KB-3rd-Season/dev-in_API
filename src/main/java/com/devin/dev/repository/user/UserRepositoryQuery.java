package com.devin.dev.repository.user;

import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryQuery {

    List<User> findUsers();

    Optional<UserSimpleDto> findUserDtoById(Long userId);

}
