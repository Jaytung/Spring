package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_user", referencedColumnName = "_id", nullable = false)
    private SystemUser createUser;

    @Column(name = "cr_datetime", nullable = false)
    private LocalDateTime createDateTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_user", referencedColumnName = "_id", nullable = false)
    private SystemUser updateUser;

    @Column(name = "up_datetime", nullable = false)
    private LocalDateTime updateDateTime = LocalDateTime.now();
}
