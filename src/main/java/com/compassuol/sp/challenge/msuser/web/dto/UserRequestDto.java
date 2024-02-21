package com.compassuol.sp.challenge.msuser.web.dto;

import java.util.Date;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Campo 'firstName' não pode ser vazio")
    @Size(min = 3)
    private String firstName;
    @NotBlank(message = "Campo 'firstName' não pode ser vazio")
    @Size(min = 3)
    private String lastName;
    @NotBlank(message = "Campo 'cpf' não pode ser vazio")
    @CPF
    @Size(min = 11, max = 11)
    private String cpf;
    @NotNull(message = "Campo data de nascimento não pode ser vazio")
    @Past
    private Date birthdate;
    @NotNull(message = "Campo 'email' não pode ser vazio")
    @Email(message = "Formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;
    @NotBlank(message = "Campo'password' não pode ser vazio")
    @Size(min = 6, message = "A senha deve possuir mais que 6 caracteres")
    private String password;
    private boolean active; 
}
