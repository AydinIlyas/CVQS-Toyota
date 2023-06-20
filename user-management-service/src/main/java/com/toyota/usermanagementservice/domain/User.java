package com.toyota.usermanagementservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.Set;

/**
 * Users class represents users entity in database
 */
@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=FALSE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private Set<Role> role;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean deleted;

}
