package com.compassuol.sp.challenge.msuser.web.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate; 
    private String email;
    private boolean active;
}
