package com.ead.authUser.services.impl;

import com.ead.authUser.models.UserCourseModel;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.repositories.UserCourseRepository;
import com.ead.authUser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Override
    public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId( userModel, courseId );
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        return userCourseRepository.save( userCourseModel );
    }
}
