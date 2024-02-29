package com.compassuol.sp.challenge.msuser.integration;

import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_MAKE_LOGIN;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_REQUEST;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_UPDATE_FIELDS;
import static com.compassuol.sp.challenge.msuser.common.UserConstants.VALID_USER_UPDATE_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.compassuol.sp.challenge.msuser.jwt.JwtTokenProvider;
import com.compassuol.sp.challenge.msuser.service.UserService;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserRequestDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdatePasswordDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = "USER")
public class UserIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Order(1)
    public void userRegister_withSuccess() throws Exception {
        UserRequestDto requestBody = VALID_USER_REQUEST;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isCreated());
        assertThat(requestBody.getEmail()).isEqualTo(VALID_USER_REQUEST.getEmail());
        assertThat(requestBody.getCpf()).isEqualTo(VALID_USER_REQUEST.getCpf());
        assertThat(requestBody).isNotNull();
    }

    @Test
    @Order(2)
    public void userRegister_withInvalidEmail() throws Exception {
        UserRequestDto requestBody = VALID_USER_REQUEST;
        requestBody.setEmail("invalid-email");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message", containsString("Campos Inválidos")));

    }

    @Test
    @Order(3)
    public void userRegister_withInvalidCpf() throws Exception {
        UserRequestDto requestBody = VALID_USER_REQUEST;
        requestBody.setCpf("invalid-cpf");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message", containsString("Campos Inválidos")));
    }

    @Test
    @Order(4)
    public void login_withSuccess() throws Exception {
        UserMakeLoginDto requestBody = VALID_USER_MAKE_LOGIN;
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isOk());
        assertThat(requestBody.getEmail()).isEqualTo(VALID_USER_MAKE_LOGIN.getEmail());
        assertThat(requestBody.getPassword()).isEqualTo(VALID_USER_MAKE_LOGIN.getPassword());
        assertThat(requestBody).isNotNull();

    }

    @Test
    @Order(5)
    public void login_withInvalidEmail() throws Exception {
        UserRequestDto requestBody = VALID_USER_REQUEST;
        requestBody.setEmail("invalid-email");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message", containsString("Campos Inválidos")));

    }

    @Test
    @Order(6)
    public void getUserById_withSuccess() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(9)
    public void updateUserFields_withSuccess() throws Exception {
        UserUpdateFieldsDto requestBody = VALID_USER_UPDATE_FIELDS;
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isOk());
        assertThat(requestBody.getFirstName()).isEqualTo(VALID_USER_UPDATE_FIELDS.getFirstName());
        assertThat(requestBody).isNotNull();
    }

    @Test
    @Order(7)
    public void updateUserFields_withInvalidCpf() throws Exception {
        UserUpdateFieldsDto requestBody = VALID_USER_UPDATE_FIELDS;
        requestBody.setEmail("invalid-email");
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message", containsString("Campos Inválidos")));
    }

    @Test
    @Order(8)
    public void updateUserPassword_withInvalidPassword() throws Exception {
        UserUpdatePasswordDto requestBody = VALID_USER_UPDATE_PASSWORD;
        requestBody.setPassword("123");
        mockMvc.perform(put("/api/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestBody)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("message", containsString("Campos Inválidos")));
    }
}
