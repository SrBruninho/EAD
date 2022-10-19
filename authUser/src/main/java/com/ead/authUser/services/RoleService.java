package com.ead.authUser.services;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;

import java.util.Optional;

public interface RoleService {

    Optional<RoleModel> findByRoleType(RoleType roleType );

}
