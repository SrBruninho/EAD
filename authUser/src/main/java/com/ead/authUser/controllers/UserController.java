package com.ead.authUser.controllers;

import com.ead.authUser.dtos.UserDTO;
import com.ead.authUser.models.UserModel;
import com.ead.authUser.services.UserService;
import com.ead.authUser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins="*", maxAge = 3600)
@RequestMapping("/users")
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0,size=10,sort="userId",direction = Sort.Direction.ASC)Pageable pageable) {
        log.debug("GET | getAllUsers");
        Page<UserModel> userModelPage = userService.findAll( pageable, spec );

        if( !userModelPage.isEmpty() ){
            for( UserModel user : userModelPage.toList() ){
                user.add( linkTo( methodOn( UserController.class ).getOneUser( user.getUserId() ) ).withSelfRel() );
            }
        }
        log.debug("GET | getAllUsers");
        log.info("GET | getAllUsers");
        return ResponseEntity.status(HttpStatus.OK).body( userModelPage );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value="userId")UUID userId){
        log.debug("GET | getOneUser userId {}", userId );
        Optional<UserModel> userModelOptional = userService.findById( userId );
        if(userModelOptional.isEmpty()){
            log.warn("User not found! {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "User not found !" );
        } else{
            log.debug("GET | getOneUser userId {} found!", userId );
            log.info("GET | getOneUser userId {} found!", userId );
            return ResponseEntity.status(HttpStatus.OK).body( userModelOptional.get() );
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value="userId") UUID userId){
        log.debug("DELETE | deleteUSER userId {}", userId );
        Optional<UserModel> userModelOptional = userService.findById( userId );
        if(userModelOptional.isEmpty()){
            log.debug("DELETE | deleteUSER userId {} not found", userId );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "User not found !" );
        } else{
            userService.deleteAndPublishEvent(userModelOptional.get());
            log.info("DELETE | deleteUSER userId {} User deleted!", userId );
            log.debug("DELETE | deleteUSER userId {} User deleted!", userId );

            return ResponseEntity.status(HttpStatus.OK).body( "User deleted!" );
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value="userId")
                                             UUID userId,
                                             @RequestBody
                                             @Validated(UserDTO.UserView.UserPut.class)
                                             @JsonView(UserDTO.UserView.UserPut.class)
                                             UserDTO userDTO){
        log.debug("PUT | updateUSER userDTO received {}", userDTO.toString() );
        Optional<UserModel> userModelOptional = userService.findById( userId );
        if(userModelOptional.isEmpty()){
            log.info("PUT | updateUSER userModel {} not found", userDTO.toString() );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "User not found !" );
        } else{
            var userModel = userModelOptional.get();
            userModel.setFullName( userDTO.getFullName() );
            userModel.setPhoneNumber( userDTO.getPhoneNumber() );
            userModel.setCpf( userDTO.getCpf() );
            userModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of( "UTC" ) ) );

            userService.updateUserAndPublishEvent( userModel );
            log.debug("PUT | updateUSER userId saved {}", userModel.getUserId() );
            log.info("PUT | updateUSER userId saved {}", userModel.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body( userModel );
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value="userId")
                                                 UUID userId,
                                                 @RequestBody
                                                 @Validated(UserDTO.UserView.PasswordPut.class)
                                                 @JsonView(UserDTO.UserView.PasswordPut.class)
                                                 UserDTO userDTO){
        log.debug("PUT | updatePassword userDTO received {}", userDTO.toString() );
        Optional<UserModel> userModelOptional = userService.findById( userId );
        if(userModelOptional.isEmpty()){
            log.warn("User not found !{} ", userId );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "User not found !" );
        } if( !userModelOptional.get().getPassword().equals( userDTO.getOldPassword())){
            log.warn("Mismatched old password! {} ", userId );
            return ResponseEntity.status(HttpStatus.CONFLICT).body( "Error: Mismatched old password!" );
        }else{
            var userModel = userModelOptional.get();
            userModel.setPassword( userDTO.getPassword() );
            userModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of( "UTC" ) ) );
            userService.updatePassword( userModel );
            log.debug("Password changed!", userId );
            log.info("Password changed!" );
            return ResponseEntity.status(HttpStatus.OK).body( "Password updated !" );
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value="userId")
                                                 UUID userId,
                                                 @RequestBody
                                                 @Validated(UserDTO.UserView.ImagePut.class)
                                                 @JsonView(UserDTO.UserView.ImagePut.class)
                                                 UserDTO userDTO){
        log.debug("PUT | updateImage userDTO received {}", userDTO.toString() );
        Optional<UserModel> userModelOptional = userService.findById( userId );
        if(userModelOptional.isEmpty()){
            log.warn("User not found !{} ", userId );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( "User not found !" );
        } else{
            var userModel = userModelOptional.get();
            userModel.setImageURL( userDTO.getImageURL() );
            userModel.setLastUpdateDate( LocalDateTime.now( ZoneId.of( "UTC" ) ) );
            userService.updateUserAndPublishEvent( userModel );
            log.debug("Image changed!", userId );
            log.info("Image changed!" );
            return ResponseEntity.status(HttpStatus.OK).body( userModel );
        }
    }

}