package com.toyota.verificationauthorizationservice.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="Roles")
public class Role {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns=@JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="permission_id")
    )
    private Set<Permission> permissions;

    public Set<GrantedAuthority> getPermissions() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
    }
}
