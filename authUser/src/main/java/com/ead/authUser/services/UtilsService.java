package com.ead.authUser.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;

import java.util.UUID;

public interface UtilsService {
    String createURLGetAllCoursesByUser(UUID userId, Pageable pageable);

    String createURLdeleteUserInCourse(UUID userId);

    HttpEntity<String> getRequestEntityToken(String token );
}
