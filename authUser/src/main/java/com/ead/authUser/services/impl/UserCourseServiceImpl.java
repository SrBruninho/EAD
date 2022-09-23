package com.ead.authUser.services.impl;

import com.ead.authUser.repositories.UserCourseRepository;
import com.ead.authUser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;
}
