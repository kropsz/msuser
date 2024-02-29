package com.compassuol.sp.challenge.msuser.common;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.compassuol.sp.challenge.msuser.enumerate.Role;
import com.compassuol.sp.challenge.msuser.model.Address;
import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserRequestDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserResponseDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdatePasswordDto;

public class UserConstants {

        public static String encryptPassword(String password) {
                return new BCryptPasswordEncoder().encode(password);
        }

        public static final LocalDate CURRENT_DATE = LocalDate.of(1992, 12, 01);
        public static final String password = "maclaren";
        public static final String passwordEncode = encryptPassword(password);
        public static final String updatePassword = "ferrari";
        public static final String passwordUpdateEncode = encryptPassword(updatePassword);

        public static final UserRequestDto VALID_USER_REQUEST = new UserRequestDto(
                        "Ayrton",
                        "Senna",
                        "61575923033",
                        CURRENT_DATE,
                        "seninha@gmail.com",
                        "12314151",
                        password,
                        true);

        public static final UserResponseDto VALID_USER_RESPONSE = new UserResponseDto(
        1l,                
        "Ayrton",
                        "Senna",
                        "61575923033",
                        CURRENT_DATE,
                        "seninha@gmail.com",
                        true,
                        new Address()

        );

        public static final User VALID_USER = new User(
                        1L,
                        Role.USER,
                        "Ayrton",
                        "Senna",
                        "61575923033",
                        CURRENT_DATE,
                        "seninha@gmail.com",
                        "12314151",
                        passwordEncode,
                        true);

        public static final UserMakeLoginDto VALID_USER_MAKE_LOGIN = new UserMakeLoginDto(
                        "seninha@gmail.com",
                        password);

        public static final UserUpdateFieldsDto VALID_USER_UPDATE_FIELDS = new UserUpdateFieldsDto(
                        "Ayrton",
                        "Senna",
                        "61575923033",
                        CURRENT_DATE,
                        "seninha@gmail.com",
                        "12314151",
                        true);

        public static final UserUpdatePasswordDto VALID_USER_UPDATE_PASSWORD = new UserUpdatePasswordDto(
                        updatePassword);

        public static final User VALID_UPDATED_USER = new User(
                1L,
                Role.USER,
                "Ayrton",
                "Senna",
                "61575923033",
                CURRENT_DATE,
                "seninha@gmail.com",
                "12314151",
                passwordUpdateEncode,
                true);
}
