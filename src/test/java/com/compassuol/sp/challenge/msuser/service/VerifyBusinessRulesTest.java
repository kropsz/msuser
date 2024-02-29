package com.compassuol.sp.challenge.msuser.service;

import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.compassuol.sp.challenge.msuser.repository.UserRepository;
import com.compassuol.sp.challenge.msuser.service.business.VerifyBusinessRules;

@ExtendWith(MockitoExtension.class)
public class VerifyBusinessRulesTest {

    @InjectMocks
    private VerifyBusinessRules verifyBusinessRules;

    @Mock
    private UserRepository userRepository;

    @Test
    public void verifyIfCredentialsExists_Successul() {
        when(userRepository.existsByCpfOrEmail(VALID_USER.getCpf(), VALID_USER.getEmail())).thenReturn(false);
        boolean result = verifyBusinessRules.verifyIfCredentialsExists(VALID_USER);
        assertEquals(false, result);
    }

    @Test
    public void verifyIfCredentialsExists_Fail() {
        when(userRepository.existsByCpfOrEmail(VALID_USER.getCpf(), VALID_USER.getEmail())).thenReturn(true);
        boolean result = verifyBusinessRules.verifyIfCredentialsExists(VALID_USER);
        assertEquals(true, result);
    }

    @Test
    public void checkPasswordIsEqual_Successful() {
        String rawPassword = "senhaTeste";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(verifyBusinessRules.checkPasswordIsEqual(rawPassword, encodedPassword));

    }
}