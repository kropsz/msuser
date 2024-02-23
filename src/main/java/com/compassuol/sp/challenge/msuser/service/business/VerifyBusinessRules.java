package com.compassuol.sp.challenge.msuser.service.business;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerifyBusinessRules {
    private final UserRepository userRepository;

    public boolean verifyIfCredentialsExists(User user) {
        return userRepository.existsByCpfOrEmail(user.getCpf(), user.getEmail());
    }

    public boolean checkPasswordIsEqual(String password, String passwordEncode){
        return new BCryptPasswordEncoder().matches(password, passwordEncode);
    }
}
