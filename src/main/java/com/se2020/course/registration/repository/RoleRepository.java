package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRole(@Param("role") String role);
}
