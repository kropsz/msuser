package com.compassuol.sp.challenge.msuser.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.compassuol.sp.challenge.msuser.model.User;
import com.compassuol.sp.challenge.msuser.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private PathMatcher pathMatcher = new AntPathMatcher();
    private final JwtTokenProvider tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (isPathMatch(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            var token = this.recoverToken(request);
            if (token != null) {
                log.info("Token recuperado");
                var login = tokenService.validateToken(token);
                Optional<User> user = userRepository.findByEmail(login);
                if (user.isPresent()) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null,
                            user.get().getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Autenticação definida para o usuário: {}", login);
                } else {
                    log.warn("Usuário não encontrado para o login: {}", login);
                }
            }

            filterChain.doFilter(request, response);

        } catch (RuntimeException ex) {
            log.error("Acesso negado", ex);
            throw new RuntimeException(ex.getMessage());
        } catch (Exception ex) {
            log.error("Erro ao filtrar a solicitação", ex);
            throw new RuntimeException(ex);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            logger.warn("Nenhum cabeçalho de autorização na solicitação");
            return null;
        }
        var token = authHeader.replace("Bearer ", "");
        log.info("Token recuperado do cabeçalho de autorização");
        return token;
    }

    private boolean isPathMatch(String requestURI) {
        return Arrays.stream(SecurityConfiguration.DOCUMENTATION_OPENAPI)
            .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }
}
