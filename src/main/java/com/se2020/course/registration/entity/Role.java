package com.se2020.course.registration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String role;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name= "role_permission",
            joinColumns = {@JoinColumn(name= "role_id")},
            inverseJoinColumns = {@JoinColumn(name= "permission_id")})
    @JsonIgnoreProperties("roles")
    private Set<Permission> permissions;


    public Role(String role){
        this.role = role;
    }

    public void addPermission(Permission permission){
        if (this.permissions == null){
            this.permissions = new HashSet<>();
        }
        this.getPermissions().add(permission);
    }

    public void removePermission(Permission permission){
        this.permissions.remove(permission);
    }

}
