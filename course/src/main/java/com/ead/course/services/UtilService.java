package com.ead.course.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilService {

    String createURLGetAllUserByCourse(UUID courseId, Pageable pageable);

    String createURLgetOneUserById(UUID userId);

    String createURLPostSubscriptionUserInCourse(UUID userId);
}
