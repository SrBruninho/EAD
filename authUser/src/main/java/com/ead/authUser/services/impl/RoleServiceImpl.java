package com.ead.authUser.services.impl;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;
import com.ead.authUser.repositories.RoleRepository;
import com.ead.authUser.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<RoleModel> findByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType( roleType );
    }

    @Transactional
    @Override
    public void updateUserRole(UUID userId, UUID roleId) {
        roleRepository.updateUserRole( userId, roleId );
    }
}
