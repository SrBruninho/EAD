package com.ead.authUser.repositories;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {

    Optional<RoleModel> findByRoleType(RoleType roleType );
}
