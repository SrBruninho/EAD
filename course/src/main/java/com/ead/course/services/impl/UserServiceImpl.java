package com.ead.course.services.impl;

import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> userSpecification, Pageable pageable) {
        return userRepository.findAll( userSpecification, pageable );
    }

    @Override
    public void save(UserModel userModel) {
        userRepository.save( userModel );
    }

    @Override
    public void delete(UUID userId) {
        userRepository.deleteById( userId );
    }

    @Override
    public Optional<UserModel> findById(UUID userInstructor) {
        return userRepository.findById( userInstructor );
    }
}