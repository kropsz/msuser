package com.compassuol.sp.challenge.msuser.service.business;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.compassuol.sp.challenge.msuser.exception.UserNotFoundException;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VerifyBusinessRules {
    private final UserRepository userRepository;

    public boolean verifyIfCredentialsExists(User user) {
        return userRepository.existsByCpfOrEmail(user.getCpf(), user.getEmail());
    }

    public User checkIfUserExists(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public boolean checkPasswordIsEqual(String password, String passwordEncode){
       return encryptPassword(password).equals(passwordEncode);
    }
}
