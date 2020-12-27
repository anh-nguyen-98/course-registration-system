package com.se2020.course.registration.controller;

import com.se2020.course.registration.entity.Permission;
import com.se2020.course.registration.entity.Role;
import com.se2020.course.registration.repository.PermissionRepository;
import com.se2020.course.registration.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
public class RolePermissionController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/access")
    public List<Role> getAllAccess(){
        return roleRepository.findAll();

    }

    @GetMapping("/access/{roleId}")
    public Role getAccess(@PathVariable(name = "roleId") Long id){
        return roleRepository.findById(id).orElse(null);
    }

    @PostMapping("/access")
    public String addRole(@RequestBody Role role){
        List<Role> roles = roleRepository.findByRole(role.getRole());
        if (roles.size() == 0){
            roleRepository.save(role);
        }
        return "Success";
    }

    @PutMapping("/access/{roleId}")
    public Role updateAccess(@PathVariable(name = "roleId") Long id, @RequestBody Permission permission){
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null){
            List<Permission> permissions = permissionRepository.findByPermission(permission.getPermission());
            if (permissions.size() > 0){
                role.addPermission(permissions.get(0));
            } else{
                role.addPermission(permission);
            }
            roleRepository.save(role);
        }
        return role;
    }


    @DeleteMapping("/access/{roleId}")
    public Role deleteAccess(@PathVariable(name = "roleId") Long roleId, @RequestParam("permissionId") Long permissionId ){
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            Permission permission = permissionRepository.findById(permissionId).orElse(null);
            if (permission != null) {
                role.removePermission(permission);
                roleRepository.save(role);
            }
        }
        return role;
    }



}
