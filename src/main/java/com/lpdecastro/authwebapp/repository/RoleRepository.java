package com.lpdecastro.authwebapp.repository;

import com.lpdecastro.authwebapp.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
