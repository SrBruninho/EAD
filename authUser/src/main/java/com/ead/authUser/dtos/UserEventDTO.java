package com.ead.authUser.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEventDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageURL;
    private String actionType;

}
