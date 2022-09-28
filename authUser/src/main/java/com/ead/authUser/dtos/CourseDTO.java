package com.ead.authUser.dtos;

import com.ead.authUser.enums.CourseLevel;
import com.ead.authUser.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CourseDTO {

    private UUID courseId;

     private String name;

    private String description;

    private String imageURL;

    private CourseStatus courseStatus;

    private UUID userInstructor;

    private CourseLevel courseLevel;

}
