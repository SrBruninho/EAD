package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
public class CourseUserController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(
            SpecificationTemplate.UserSpec userSpec,
            @PageableDefault(page = 0,
                    size=10,sort="userId",
                    direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value="courseId") UUID courseId ) {
        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.findAll( SpecificationTemplate.userCourseId( courseId).and( userSpec ), pageable ) );
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDTO subscriptionDTO){

        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        // TO DO with STATE TRANSFER
        return ResponseEntity.status( HttpStatus.CREATED ).body( "TO DO" );
    }
}
