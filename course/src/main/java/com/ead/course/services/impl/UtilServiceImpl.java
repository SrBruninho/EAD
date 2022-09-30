package com.ead.course.services.impl;

import com.ead.course.services.UtilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilServiceImpl implements UtilService {

    public String createURLGetAllUserByCourse(UUID courseId, Pageable pageable){

        return  "/users?courseId=" + courseId +"&page=" + pageable.getPageNumber()
                +"&size="+pageable.getPageSize()+"&sort="+pageable.getSort().toString()
                .replaceAll(": ",",");
    }

    @Override
    public String createURLgetOneUserById(UUID userId) {
        return  "/users/" + userId;
    }

    @Override
    public String createURLPostSubscriptionUserInCourse(UUID userId) {
        return "/users/" + userId +"/courses/subscription";
    }

    @Override
    public String createURLdeleteCourseInAuthUser(UUID courseId) {
        return "/users/courses/" + courseId;
    }
}
