package com.compassuol.sp.challenge.msuser.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMakeLoginDto {
    @NotBlank
    @Email(message = "Formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;
}
