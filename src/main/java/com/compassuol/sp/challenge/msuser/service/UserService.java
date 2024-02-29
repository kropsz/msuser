package com.compassuol.sp.challenge.msuser.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compassuol.sp.challenge.msuser.enumerate.EventEnum;
import com.compassuol.sp.challenge.msuser.exception.BusinessViolationException;
import com.compassuol.sp.challenge.msuser.exception.UserNotFoundException;
import com.compassuol.sp.challenge.msuser.feign.AddressFeign;
import com.compassuol.sp.challenge.msuser.jwt.JwtTokenProvider;
import com.compassuol.sp.challenge.msuser.model.Address;
import com.compassuol.sp.challenge.msuser.model.Event;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.mqueue.EventNotificationPublisher;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;
import com.compassuol.sp.challenge.msuser.service.business.VerifyBusinessRules;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserResponseDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerifyBusinessRules checkRules;
    private final JwtTokenProvider tokenService;
    private final EventNotificationPublisher eventPublisher;
    private final AddressFeign addressFeign;

    @Transactional
    public UserResponseDto registerUser(User user) {
        if (checkRules.verifyIfCredentialsExists(user))
            throw new BusinessViolationException("Email ou CPF ja existem");
        user.setPassword(encryptPassword(user.getPassword()));
        try {
            Event event = new Event(user.getEmail(), EventEnum.CREATE, null);
            eventPublisher.eventNotification(event);

        } catch (JsonProcessingException ex) {
            throw new BusinessViolationException("Erro Enviando mensagem para fila de eventos !");
        }
        userRepository.save(user);
        return setAddress(user);

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
    public UserResponseDto getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return setAddress(user);
    }

    @Transactional
    public UserResponseDto updateUserFields(Long id, UserUpdateFieldsDto updateDto) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        User updateUser = userRepository.save(UserMapper.toUserFromUpdateUser(updateDto, user));
        try {
            Event event = new Event(updateUser.getEmail(), EventEnum.UPDATE, null);
            eventPublisher.eventNotification(event);
        } catch (Exception e) {
            throw new BusinessViolationException("Erro enviando mensagem para fila de eventos !");
        }
        return setAddress(updateUser);
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

    public UserResponseDto setAddress(User user) {
        Address address = addressFeign.getAddressByCep(user.getCep());
        UserResponseDto response = UserMapper.tDto(user);
        response.setAddress(address);
        return response;
    }
}
