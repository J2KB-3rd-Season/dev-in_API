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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated
    private  ServiceType type;

    private String title;
    private String content;

    @Enumerated
    private ServiceState status;
}
