package com.ead.authUser.services.impl;

import com.ead.authUser.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {
    public String createURLGetAllCoursesByUser(UUID userId, Pageable pageable){

        return  "/courses?userId=" + userId +"&page=" + pageable.getPageNumber()
                +"&size="+pageable.getPageSize()+"&sort="+pageable.getSort().toString()
                .replaceAll(": ",",");
    }

    @Override
    public String createURLdeleteUserInCourse(UUID userId) {
        return "/courses/users/" + userId;
    }

    @Override
    public HttpEntity<String> getRequestEntityToken( String token ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set( "Authorization", token );
        return new HttpEntity<String>("parameters", httpHeaders );
    }
}