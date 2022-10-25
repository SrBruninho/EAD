package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(origins="*",maxAge=3600)
@Log4j2
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseValidator courseValidator;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDTO courseDTO, Errors errors){
        log.debug("POST | saveCourse courseDTO received {}", courseDTO.toString() );

        courseValidator.validate( courseDTO, errors );
        if( errors.hasErrors()){
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( errors.getAllErrors() );
        }
        var courseModel = new CourseModel();

        BeanUtils.copyProperties(courseDTO,courseModel);
        courseModel.setCreationDate( LocalDateTime.now( ZoneId.of("UTC") ) );
        courseModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of("UTC") ) );
        log.debug("POST | saveCourse courseDTO saved {}", courseModel.toString() );
        log.info("Course saved succesfully courseId {}", courseModel.getCourseId() );

        return ResponseEntity.status( HttpStatus.CREATED ).body( courseService.save( courseModel ) );

    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId){

        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        courseService.delete( courseModelOptional.get() );
        return ResponseEntity.status( HttpStatus.OK ).body( "Course deleted!" );
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
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

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec courseSpec,
                                                           @PageableDefault(
                                                                   page=0,size = 10,sort = "courseId",
                                                                   direction = Sort.Direction.ASC)Pageable pageable,
    @RequestParam(required = false) UUID userId){

        if( userId != null){
            return ResponseEntity.status( HttpStatus.OK ).body(
                    courseService.findAll( SpecificationTemplate.courseUserId( userId ).and( courseSpec ), pageable) );
        }else {
            return ResponseEntity.status( HttpStatus.OK ).body( courseService.findAll( courseSpec, pageable) );
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById( courseId );
        if( courseModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }
        return ResponseEntity.status( HttpStatus.OK ).body( courseModelOptional.get() );
    }
}
