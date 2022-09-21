package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid ModuleDTO moduleDTO){
        Optional<CourseModel> optionalCourseModel = courseService.findById( courseId );

        if(optionalCourseModel.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Course Not Found !" );
        }

        var moduleModel = new ModuleModel();

        BeanUtils.copyProperties(moduleDTO,moduleModel);
        moduleModel.setCreationDate( LocalDateTime.now( ZoneId.of("UTC") ) );
        moduleModel.setCourse( optionalCourseModel.get() );
        return ResponseEntity.status( HttpStatus.CREATED ).body( moduleService.save( moduleModel ) );

    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){

        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse( courseId, moduleId );
        if( optionalModuleModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Module Not Found for this Course!" );
        }
        moduleService.delete( optionalModuleModel.get() );
        return ResponseEntity.status( HttpStatus.OK ).body( "Module deleted!" );
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDTO moduleDTO){
        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse( courseId, moduleId );
        if( optionalModuleModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Module Not Found for this Course!" );
        }

        var moduleModel = optionalModuleModel.get();
        moduleModel.setTitle( moduleDTO.getTitle()  );
        moduleModel.setDescription( moduleDTO.getDescription() );

        return ResponseEntity.status( HttpStatus.OK ).body( moduleService.save( moduleModel ) );
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.status( HttpStatus.OK ).body( moduleService.findAllByCourse( courseId ) );
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                               @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse( courseId,moduleId );
        if( optionalModuleModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Module Not Found !" );
        }
        return ResponseEntity.status( HttpStatus.OK ).body( optionalModuleModel.get() );
    }

}
