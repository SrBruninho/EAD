package com.ead.authUser.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {
    String createURLGetAllCoursesByUser(UUID userId, Pageable pageable);

    String createURLdeleteUserInCourse(UUID userId);
}
