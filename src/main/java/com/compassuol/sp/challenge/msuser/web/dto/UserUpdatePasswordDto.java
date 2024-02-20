package com.compassuol.sp.challenge.msuser.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdatePasswordDto {
    @NotBlank(message = "Campo 'password' não pode ser vazio")
    @Size(min = 6, message = "A Senha deve possuir no minimo 6 caracteres")
    private String password;
}
