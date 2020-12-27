package com.se2020.course.registration.repository;

import com.se2020.course.registration.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByPermission(@Param("permission") String p);
}
