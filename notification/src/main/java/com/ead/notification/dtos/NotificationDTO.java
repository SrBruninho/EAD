package com.ead.notification.dtos;

import com.ead.notification.Enums.NotificationStatus;

import javax.validation.constraints.NotNull;

public class NotificationDTO {

    @NotNull
    private NotificationStatus notificationStatus;

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
