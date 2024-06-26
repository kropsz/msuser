package com.compassuol.sp.challenge.msuser.web.dto;

import java.time.LocalDate;

import com.compassuol.sp.challenge.msuser.model.Address;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;
    private String email;
    private boolean active;
    private Address address;
}
