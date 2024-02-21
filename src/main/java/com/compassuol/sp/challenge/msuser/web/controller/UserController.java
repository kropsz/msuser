package com.compassuol.sp.challenge.msuser.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compassuol.sp.challenge.msuser.service.UserService;
import com.compassuol.sp.challenge.msuser.web.dto.UserMakeLoginDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserRequestDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserResponseDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdateFieldsDto;
import com.compassuol.sp.challenge.msuser.web.dto.UserUpdatePasswordDto;
import com.compassuol.sp.challenge.msuser.web.dto.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDto> userRegister(@RequestBody @Valid UserRequestDto dto){
        var entity = userService.registerUser(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.tDto(entity));
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserMakeLoginDto login){
      return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(login));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
      var user = userService.getUserById(id);
      return ResponseEntity.status(HttpStatus.OK).body(UserMapper.tDto(user));

    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserFields(@PathVariable Long id, 
              @RequestBody @Valid UserUpdateFieldsDto updateDto){
      var user = userService.updateUserFields(id, updateDto);
      return ResponseEntity.ok().body(UserMapper.tDto(user));
    }

    
    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponseDto> updateUserPassword(@PathVariable Long id, 
              @RequestBody @Valid UserUpdatePasswordDto passwordDto){
      var user = userService.updateUserPassword(id, passwordDto.getPassword());
      return ResponseEntity.ok().body(UserMapper.tDto(user));
    }
}
