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

    private String username;

    private String email;
    @Enumerated(value = EnumType.STRING)
    private Set<Role> role;
    @Enumerated(value=EnumType.STRING)
    private Gender gender;

    private boolean deleted;

}
