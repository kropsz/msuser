package com.compassuol.sp.challenge.msuser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.compassuol.sp.challenge.msuser.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    boolean existsByCpfOrEmail(String cpf, String email);
    Optional<User> findByEmail(String email);

}
