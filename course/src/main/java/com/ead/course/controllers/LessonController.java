package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDTO lessonDTO){
        Optional<ModuleModel> optionalModuleModel = moduleService.findById( moduleId );

        if(optionalModuleModel.isEmpty()){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Module Not Found !" );
        }

        var lessonModel = new LessonModel();

        BeanUtils.copyProperties(lessonDTO,lessonModel);
        lessonModel.setCreationDate( LocalDateTime.now( ZoneId.of("UTC") ) );
        lessonModel.setModule( optionalModuleModel.get() );
        return ResponseEntity.status( HttpStatus.CREATED ).body( lessonService.save( lessonModel ) );

    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){

        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule( moduleId, lessonId );
        if( optionalLessonModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Lesson Not Found for this Module!" );
        }
        lessonService.delete( optionalLessonModel.get() );
        return ResponseEntity.status( HttpStatus.OK ).body( "Lesson deleted!" );
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDTO lessonDTO){
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule( moduleId, lessonId );
        if( optionalLessonModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Lesson Not Found for this Module!" );
        }

        var lessonModel = optionalLessonModel.get();
        BeanUtils.copyProperties(lessonDTO,lessonModel);
        return ResponseEntity.status( HttpStatus.OK ).body( lessonService.save( lessonModel ) );
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec lessonSpec,
                                                           @PageableDefault(
                                                                   page=0,size = 10,sort = "lessonId",
                                                                   direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status( HttpStatus.OK ).body( lessonService.findAllByModule( lessonSpec, pageable ) );
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId){
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule( moduleId,lessonId );
        if( optionalLessonModel.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Lesson Not Found !" );
        }
        return ResponseEntity.status( HttpStatus.OK ).body( optionalLessonModel.get() );
    }
}
