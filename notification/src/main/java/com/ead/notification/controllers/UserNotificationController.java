package com.ead.notification.controllers;

import com.ead.notification.dtos.NotificationDTO;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class UserNotificationController {

    final NotificationService notificationService;

    public UserNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(
            @PathVariable(value = "userId") UUID userId,
            @PageableDefault(page = 0,size = 10,sort = "notificationId", direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication)
    {
        return ResponseEntity.status( HttpStatus.OK )
                .body( notificationService.findAllNotificationsByUser( userId, pageable ) );
    }

    @PutMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(
            @PathVariable(value = "userId") UUID userId,
            @PathVariable(value = "notificationId") UUID notificationId,
            @RequestBody @Valid NotificationDTO notificationDTO)
    {
        Optional<NotificationModel> notificationModelOpt =
                notificationService.findByNotificationIdAndUserId( notificationId, userId );

        if( notificationModelOpt.isEmpty() )
            return  ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Notification not Found!" );

        notificationModelOpt.get().setNotificationStatus( notificationDTO.getNotificationStatus() );
        notificationService.saveNotification( notificationModelOpt.get() );

        return ResponseEntity.status( HttpStatus.OK )
                .body( notificationModelOpt.get() );
    }


}
