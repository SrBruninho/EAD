package com.ead.course.validation;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseDTO;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationCurrentUserService authCurrentUser;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) target;
        validator.validate( courseDTO, errors );
        if( !errors.hasErrors() ){
            validateUserInstructor( courseDTO.getUserInstructor(), errors );
        }
    }

    private void validateUserInstructor( UUID userInstructor, Errors errors ){
        UUID currentUserId = authCurrentUser.getCurrentUser().getUserId();
        if( currentUserId.equals( userInstructor )){
            Optional<UserModel> userModel = userService.findById( userInstructor );
            if( userModel.isEmpty() ){
                errors.rejectValue("userInstructor","UserInstructorError",
                        "Instructor NOT FOUND!");
            }

            if( userModel.get().getUserType().equals( UserType.STUDENT.toString() ) ){
                errors.rejectValue( "userInstructor","UserInstructorError",
                        "User MUST be INSTRUCTOR or ADMIN" );
            }
        } else {
            throw new AccessDeniedException( "Forbidden" );
        }
    }
}
