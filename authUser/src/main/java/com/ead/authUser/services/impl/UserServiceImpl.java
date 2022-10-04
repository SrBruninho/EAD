package com.ead.authUser.services.impl;

import com.ead.authUser.clients.CourseClient;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.repositories.UserRepository;
import com.ead.authUser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClient courseClient;

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById( userId );
    }

    @Override
    public void deleteUser(UserModel userModel) {
        userRepository.delete( userModel );
    }

    @Override
    public void saveUser(UserModel userModel) {
        userRepository.save( userModel );
    }

    @Override
    public boolean existsByUserName(String username) {
        return userRepository.existsByUsername( username );
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail( email );
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable, Specification<UserModel> spec) {
        return userRepository.findAll( spec,pageable );
    }


}
