package com.compassuol.sp.challenge.msuser.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compassuol.sp.challenge.msuser.exception.BusinessViolationException;
import com.compassuol.sp.challenge.msuser.exception.UserNotFoundException;
import com.compassuol.sp.challenge.msuser.jwt.JwtTokenService;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;
import com.compassuol.sp.challenge.msuser.service.business.VerifyBusinessRules;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.mapper.UserMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerifyBusinessRules checkRules;
    private final JwtTokenService tokenService;

    @Transactional
    public User registerUser(User user) {
        if (checkRules.verifyIfCredentialsExists(user)) throw new BusinessViolationException("Email ou CPF ja existem");
        //TODO Implementar mensagem de Evento com o RabbitMQ
        user.setPassword(encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }
    
    @Transactional
    public String loginUser(UserMakeLoginDto user){
        Optional<User> chekUser = userRepository.findByEmail(user.getEmail());
        if (chekUser.isPresent() && checkRules.checkPasswordIsEqual(user.getPassword(), chekUser.get().getPassword())) {
            //TODO Implementar mensagem de Evento com o RabbitMQ
        return tokenService.createToken(chekUser.get());
        } else{
             throw new UserNotFoundException("Email ou senha inválidos !");
        }
    }
    
    @Transactional(readOnly = true)
    public User getUserById(Long id){
        var user = checkRules.checkIfUserExists(id);
        return user;
    }

    @Transactional
    public User updateUserFields(Long id, UserUpdateFieldsDto updateDto){
        var user = checkRules.checkIfUserExists(id);
        User updateUser = userRepository.save(UserMapper.toUserFromUpdateUser(updateDto, user));
        //TODO Implementar mensagem de Envento com o RabbitMQ
        return updateUser;
    }

    @Transactional
    public User updateUserPassword(Long id, String password){
        var user = checkRules.checkIfUserExists(id);
        user.setPassword(encryptPassword(password));
        //TODO Implementar mensagem de Envento com o RabbitMQ
        return userRepository.save(user);
    }
    
        public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
