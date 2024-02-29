package com.compassuol.sp.challenge.msuser.service;

import static com.compassuol.sp.challenge.msuser.common.EventConstants.VALID_EVENT_CREATE;
import static com.compassuol.sp.challenge.msuser.common.EventConstants.VALID_EVENT_LOGIN;
import static com.compassuol.sp.challenge.msuser.common.EventConstants.VALID_EVENT_UPDATE;
import static com.compassuol.sp.challenge.msuser.common.EventConstants.VALID_EVENT_UPDATE_PASSWORD;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_MAKE_LOGIN;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_RESPONSE;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_UPDATE_FIELDS;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.passwordEncode;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.passwordUpdateEncode;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.updatePassword;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import com.compassuol.sp.challenge.msuser.exception.BusinessViolationException;
import com.compassuol.sp.challenge.msuser.exception.UserNotFoundException;
import com.compassuol.sp.challenge.msuser.feign.AddressFeign;
import com.compassuol.sp.challenge.msuser.jwt.JwtTokenProvider;
import com.compassuol.sp.challenge.msuser.model.Address;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.mqueue.EventNotificationPublisher;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;
import com.compassuol.sp.challenge.msuser.service.business.VerifyBusinessRules;
import com.compassuol.sp.challenge.msuser.web.dto.UserResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerifyBusinessRules chekRules;

    @Mock
    private JwtTokenProvider jwtTokenService;

    @Mock
    private EventNotificationPublisher eventPublisher;

    @Mock
    private AddressFeign addressFeign;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    @Order(3)
    public void registerUser_successfull_WithValidData() throws JsonProcessingException {
        when(chekRules.verifyIfCredentialsExists(VALID_USER)).thenReturn(false);
        doNothing().when(eventPublisher).eventNotification(VALID_EVENT_CREATE);
        when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);
        UserResponseDto user = userService.registerUser(VALID_USER);
        user.setAddress(new Address());
        assertEquals(VALID_USER_RESPONSE, user);
    }

    @Test
    @Order(2)
    public void registerUser_failed_WithInvalidData() throws JsonProcessingException {
        when(chekRules.verifyIfCredentialsExists(VALID_USER)).thenReturn(true);
        assertThatThrownBy(() -> userService.registerUser(VALID_USER)).isInstanceOf(BusinessViolationException.class);
    }

    @Test
    @Order(1)
    public void loginUser_successfull_WithValidData() throws JsonProcessingException {
        when(userRepository.findByEmail(VALID_USER_MAKE_LOGIN.getEmail())).thenReturn(Optional.of(VALID_USER));
        when(chekRules.checkPasswordIsEqual(VALID_USER_MAKE_LOGIN.getPassword(), passwordEncode)).thenReturn(true);
        doNothing().when(eventPublisher).eventNotification(VALID_EVENT_LOGIN);
        when(jwtTokenService.createToken(VALID_USER)).thenReturn("token");
        String token = userService.loginUser(VALID_USER_MAKE_LOGIN);
        assertEquals("token", token);
    }

    @Test
    @Order(4)
    public void loginUser_failed_WithInvalidData() throws JsonProcessingException {
        when(userRepository.findByEmail(VALID_USER_MAKE_LOGIN.getEmail()))
                .thenReturn(java.util.Optional.of(VALID_USER));
        when(chekRules.checkPasswordIsEqual(VALID_USER_MAKE_LOGIN.getPassword(), passwordEncode)).thenReturn(false);
        assertThatThrownBy(() -> userService.loginUser(VALID_USER_MAKE_LOGIN))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Order(5)
    public void getUser_sucessful_withValidId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(VALID_USER));
        UserResponseDto user = userService.getUserById(1L);
        user.setAddress(new Address());
        assertEquals(VALID_USER_RESPONSE, user);
    }

    @Test
    @Order(6)
    public void getUser_failed_withInvalidId() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> userService.getUserById(1L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Order(7)
    public void updateUserFields_sucessful_withValidData() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(VALID_USER));
        when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);
        doNothing().when(eventPublisher).eventNotification(VALID_EVENT_UPDATE);
        UserResponseDto user = userService.updateUserFields(1L, VALID_USER_UPDATE_FIELDS);
        user.setAddress(new Address());
        assertEquals(VALID_USER_RESPONSE, user);
    }

    @Test
    @Order(8)
    public void updateUserFields_failed_withInvalidData() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> userService.updateUserFields(1L, VALID_USER_UPDATE_FIELDS))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Order(9)
    public void updateUserPassword_sucessful_withValidData() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(VALID_USER));
        User updateUser = VALID_USER;
        updateUser.setPassword(passwordUpdateEncode);
        when(userRepository.save(updateUser)).thenReturn(updateUser);
        doNothing().when(eventPublisher).eventNotification(VALID_EVENT_UPDATE_PASSWORD);
        User user = userService.updateUserPassword(1L, updatePassword);
        assertEquals(updateUser, user);
    }

    @Test
    @Order(10)
    public void updateUserPassword_failed_withInvalidData() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> userService.updateUserPassword(1L, updatePassword))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @Order(11)
    public void registerUser_Error_SendingMessageToEventQueue() throws JsonProcessingException {
        when(chekRules.verifyIfCredentialsExists(VALID_USER)).thenReturn(false);
        doThrow(new JsonProcessingException("Erro Enviando mensagem para fila de eventos !") {
        }).when(eventPublisher).eventNotification(VALID_EVENT_CREATE);
        when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);
    }
    
    @Test
    @Order(12)
    public void loginUser_Error_SendingMessageToEventQueue() throws JsonProcessingException {
        when(userRepository.findByEmail(VALID_USER_MAKE_LOGIN.getEmail())).thenReturn(Optional.of(VALID_USER));
        when(chekRules.checkPasswordIsEqual(VALID_USER_MAKE_LOGIN.getPassword(), passwordEncode)).thenReturn(true);
        doThrow(new JsonProcessingException("Erro enviando mensagem para fila de eventos !") {
        }).when(eventPublisher).eventNotification(VALID_EVENT_LOGIN);
        when(jwtTokenService.createToken(VALID_USER)).thenReturn("token");
    }

    @Test
    @Order(13)
    public void updateUserFields_Error_SendingMessageToEventQueue() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(VALID_USER));
        when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);
        doThrow(new JsonProcessingException("Erro enviando mensagem para fila de eventos !") {
        }).when(eventPublisher).eventNotification(VALID_EVENT_UPDATE);
    }

    @Test
    @Order(14)
    public void updateUserPassword_Error_SendingMessageToEventQueue() throws JsonProcessingException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(VALID_USER));
        User updateUser = VALID_USER;
        updateUser.setPassword(passwordUpdateEncode);
        when(userRepository.save(updateUser)).thenReturn(updateUser);
        doThrow(new JsonProcessingException("Erro enviando mensagem para fila de eventos !") {
        }).when(eventPublisher).eventNotification(VALID_EVENT_UPDATE_PASSWORD);
    }
}






