package com.ead.authUser.controllers;

import com.ead.authUser.dtos.InstructorDTO;
import com.ead.authUser.dtos.UserDTO;
import com.ead.authUser.enums.RoleType;
import com.ead.authUser.enums.UserStatus;
import com.ead.authUser.enums.UserType;
import com.ead.authUser.models.RoleModel;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.services.RoleService;
import com.ead.authUser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
@RequestMapping("/instructors")
@Log4j2
public class InstructorController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody
                                               @Valid
                                                             InstructorDTO instructorDTO){
        log.debug("POST | registerInstructor instructorDTO received {}", instructorDTO.toString() );

        Optional<UserModel>  userModelOptional = userService.findById( instructorDTO.getUserId() );

        Optional<RoleModel> roleModelInstructor = roleService.findByRoleType( RoleType.ROLE_INSTRUCTOR );

        if( userModelOptional.isEmpty() ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body( "User not Found!");
        } else {
            var userModel = userModelOptional.get();
            userModel.setUserType( UserType.INSTRUCTOR );
            userModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of("UTC") ) );
            roleService.updateUserRole( userModel.getUserId(), roleModelInstructor.get().getRoleId() );
            userService.updateUserAndPublishEvent( userModel );
            return ResponseEntity.status( HttpStatus.CREATED ).body( userModel );

        }
    }
}
