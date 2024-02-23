package com.compassuol.sp.challenge.msuser.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compassuol.sp.challenge.msuser.enumerate.EventEnum;
import com.compassuol.sp.challenge.msuser.exception.BusinessViolationException;
import com.compassuol.sp.challenge.msuser.exception.UserNotFoundException;
import com.compassuol.sp.challenge.msuser.jwt.JwtTokenService;
import com.compassuol.sp.challenge.msuser.model.Event;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.mqueue.EventNotificationPublisher;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;
import com.compassuol.sp.challenge.msuser.service.business.VerifyBusinessRules;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerifyBusinessRules checkRules;
    private final JwtTokenService tokenService;
    private final EventNotificationPublisher eventPublisher;

    @Transactional
    public User registerUser(User user) {
        if (checkRules.verifyIfCredentialsExists(user))
            throw new BusinessViolationException("Email ou CPF ja existem");
        user.setPassword(encryptPassword(user.getPassword()));
        try {
            Event event = new Event(user.getEmail(), EventEnum.CREATE, null);
            eventPublisher.eventNotification(event);

        } catch (JsonProcessingException ex) {
            throw new BusinessViolationException("Erro Enviando mensagem para fila de eventos !");
        }
        return userRepository.save(user);
    }

    @Transactional
    public String loginUser(UserMakeLoginDto user) {
        Optional<User> chekUser = userRepository.findByEmail(user.getEmail());
        if (chekUser.isPresent() && checkRules.checkPasswordIsEqual(user.getPassword(), chekUser.get().getPassword())) {
            try {
                Event event = new Event(user.getEmail(), EventEnum.LOGIN, null);
                eventPublisher.eventNotification(event);
            } catch (Exception e) {
                throw new BusinessViolationException("Erro enviando mensagem para fila de eventos !");
            }
            return tokenService.createToken(chekUser.get());
        } else {
            throw new UserNotFoundException("Email ou senha inválidos !");
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return user;
    }

    @Transactional
    public User updateUserFields(Long id, UserUpdateFieldsDto updateDto) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User updateUser = userRepository.save(UserMapper.toUserFromUpdateUser(updateDto, user));
        try {
            Event event = new Event(updateUser.getEmail(), EventEnum.UPDATE, null);
            eventPublisher.eventNotification(event);
        } catch (Exception e) {
            throw new BusinessViolationException("Erro enviando mensagem para fila de eventos !");
        }
        return updateUser;
    }

    @Transactional
    public User updateUserPassword(Long id, String password) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        user.setPassword(encryptPassword(password));
        userRepository.save(user);
        try {
            Event event = new Event(user.getEmail(), EventEnum.UPDATE_PASSWORD, null);
            eventPublisher.eventNotification(event);
        } catch (Exception e) {
            throw new BusinessViolationException("Erro enviando mensagem para fila de eventos !");
        }
        return user;

    }

    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
