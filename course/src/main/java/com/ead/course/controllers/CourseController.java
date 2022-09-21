package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(origins="*",maxAge=3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        var courseModel = new CourseModel();

        BeanUtils.copyProperties(courseDTO,courseModel);
        courseModel.setCreationDate( LocalDateTime.now( ZoneId.of("UTC") ) );
        courseModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of("UTC") ) );

        return ResponseEntity.status( HttpStatus.CREATED ).body( courseService.save( courseModel ) );

    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        courseService.delete( courseModelOptional.get() );
        return ResponseEntity.status( HttpStatus.OK ).body( "Course deleted!" );
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId,
                                         @RequestBody @Valid CourseDTO courseDTO){
        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }

        var courseModel = courseModelOptional.get();
        courseModel.setName( courseDTO.getName() );
        courseModel.setDescription( courseDTO.getDescription() );
        courseModel.setImageURL( courseDTO.getImageURL() );
        courseModel.setCourseStatus( courseDTO.getCourseStatus() );
        courseModel.setCourseLevel( courseDTO.getCourseLevel() );
        courseModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of("UTC") ) );

        return ResponseEntity.status( HttpStatus.OK ).body( courseService.save( courseModel ) );
    }

    @GetMapping
    public ResponseEntity<List<CourseModel>> getAllCourses(){
        return ResponseEntity.status( HttpStatus.OK ).body( courseService.findAll() );
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        return ResponseEntity.status( HttpStatus.OK ).body( courseModelOptional.get() );
    }
}
