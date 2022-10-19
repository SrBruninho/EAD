package com.ead.authUser.repositories;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {

    Optional<RoleModel> findByRoleType( RoleType roleType );

    @Modifying
    @Query(value = "insert into tb_users_roles values (:userId,:roleId);",nativeQuery = true)
    void updateUserRole(@Param("userId") UUID userId, @Param("roleId") UUID roleId);
}
