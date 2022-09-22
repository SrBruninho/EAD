package com.ead.authUser.controllers;

import com.ead.authUser.dtos.UserDTO;
import com.ead.authUser.enums.UserStatus;
import com.ead.authUser.enums.UserType;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
@RequestMapping("/auth")
@Log4j2
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                                   @Validated(UserDTO.UserView.RegistrationPost.class)
                                                   @JsonView(UserDTO.UserView.RegistrationPost.class)
                                                   UserDTO userDTO){
        log.debug("POST | registerUSER userDTO received {}", userDTO.toString() );
        if( userService.existsByUserName( userDTO.getUsername()) ){
            log.warn("Username {} already registered! ", userDTO.getUsername() );
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "Error: Username already registered!" );
        }

        if( userService.existsByEmail( userDTO.getEmail()) ){
            log.warn("E-mail {} already registered! ", userDTO.getEmail() );
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "Error: E-mail already registered!" );
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO,userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate( LocalDateTime.now(ZoneId.of("UTC")) );
        userModel.setLastUpdateDate( LocalDateTime.now(ZoneId.of("UTC")) );

        userService.saveUser( userModel );
        log.debug("POST | register USER userDTO saved {}", userModel.toString() );
        log.info("User saved succesfully ! {} ", userModel.getUserId() );
        return ResponseEntity.status( HttpStatus.CREATED ).body( userModel );
    }
}
