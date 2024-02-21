package com.compassuol.sp.challenge.msuser.web.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;
    private String email;
    private String password;
    private boolean active;
}
