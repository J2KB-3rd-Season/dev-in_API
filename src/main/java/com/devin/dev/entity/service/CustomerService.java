package com.devin.dev.entity.service;

import com.devin.dev.entity.base.Created;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerService extends Created {

    @Id
    @GeneratedValue
    @Column(name = "service_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated
    ServiceType type;

    String title;
    String content;

    @Enumerated
    ServiceState status;
}
