package com.ead.authUser.controllers;

import com.ead.authUser.clients.UserClient;
import com.ead.authUser.dtos.CourseDTO;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
public class UserCourseController {

    @Autowired
    private UserClient userClient;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(
            @PageableDefault(page = 0,
                    size=10,sort="courseId",
                    direction = Sort.Direction.ASC)Pageable pageable,
            @PathVariable(value="userId") UUID userId ) {

        return ResponseEntity.status(HttpStatus.OK).body( userClient.getAllCoursesByUser( userId, pageable));
    }
}
