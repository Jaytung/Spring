package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "system_user")
public class SystemUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "account", nullable = false, unique = true, length = 128)
    private String account;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "name", length = 128)
    private String name;
}
