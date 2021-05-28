package com.devin.dev.repository.user;

import com.devin.dev.entity.user.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
