package com.compassuol.sp.challenge.msuser.web.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String cpf;
    private String birthdate;
    private String email;
    private String password;
    private boolean active;
}
