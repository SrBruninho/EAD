package com.ead.authUser.services.impl;

import com.ead.authUser.enums.RoleType;
import com.ead.authUser.models.RoleModel;
import com.ead.authUser.repositories.RoleRepository;
import com.ead.authUser.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<RoleModel> findByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType( roleType );
    }
}
