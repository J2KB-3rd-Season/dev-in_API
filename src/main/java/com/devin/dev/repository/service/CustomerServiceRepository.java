package com.devin.dev.repository.service;

import com.devin.dev.entity.service.CustomerService;
import com.devin.dev.entity.service.ServiceState;
import com.devin.dev.entity.service.ServiceType;
import com.devin.dev.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long>, CustomerServiceRepositoryQuery {

    List<CustomerService> findByUser(User user);

    List<CustomerService> findByType(ServiceType type);

    List<CustomerService> findByTitleLike(String title);

    List<CustomerService> findByTitleLikeAndType(String title, ServiceType type);

    List<CustomerService> findByStatus(ServiceState status);
}
