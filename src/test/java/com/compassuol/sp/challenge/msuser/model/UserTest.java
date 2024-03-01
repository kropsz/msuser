package com.compassuol.sp.challenge.msuser.model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.compassuol.sp.challenge.msuser.enumerate.Role;

public class UserTest {

    @Test
    public void testUser() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.ADMIN);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCpf("12345678901");
        user.setBirthdate(LocalDate.now());
        user.setEmail("test@example.com");
        user.setCep("12345678");
        user.setPassword("password");
        user.setActive(true);

        assertEquals(1L, user.getId());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("12345678901", user.getCpf());
        assertEquals(LocalDate.now(), user.getBirthdate());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("12345678", user.getCep());
        assertEquals("password", user.getPassword());
        assertEquals(true, user.getActive());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        List<SimpleGrantedAuthority> expectedAuthorities = List.of(
                new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER"));
        assertEquals(expectedAuthorities, authorities);

        assertEquals("test@example.com", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(true, user.isAccountNonExpired());
        assertEquals(true, user.isAccountNonLocked());
        assertEquals(true, user.isCredentialsNonExpired());
        assertEquals(true, user.isEnabled());
    }
}