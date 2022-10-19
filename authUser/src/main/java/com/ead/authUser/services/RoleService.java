package com.ead.authUser.services;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleService {

    Optional<RoleModel> findByRoleType( RoleType roleType );

    void updateUserRole( UUID userId, UUID roleId );


}
