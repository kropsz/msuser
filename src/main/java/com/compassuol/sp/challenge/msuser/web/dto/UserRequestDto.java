package com.compassuol.sp.challenge.msuser.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "Campo data de nascimento não pode ser vazio")
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "Formato da data de nascimento está errado")
    private String birthdate;
    @NotBlank(message = "Campo 'email' não pode ser vazio")
    @Email(message = "Formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;
    @NotBlank(message = "Campo'password' não pode ser vazio")
    @Size(min = 6, message = "A senha deve possuir mais que 6 caracteres")
    private String password;
    private boolean active; 
}