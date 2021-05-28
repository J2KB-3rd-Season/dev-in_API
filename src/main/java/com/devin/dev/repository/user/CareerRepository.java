package com.devin.dev.repository.user;

import com.devin.dev.entity.user.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {
}
