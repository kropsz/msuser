package com.compassuol.sp.challenge.msuser.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.web.dto.UserRequestDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserResponseDto;

public class UserMapper {
    
    public static User toUser(UserRequestDto dto){
        return new ModelMapper().map(dto, User.class);
    }

    public static UserResponseDto tDto(User user){
        return new ModelMapper().map(user,UserResponseDto.class);
    }


}
