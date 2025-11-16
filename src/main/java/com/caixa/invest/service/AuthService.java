package com.caixa.invest.service;

import com.caixa.invest.domain.User;
import com.caixa.invest.security.PasswordEncoder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        User user = User.find("username", username).firstResult();
        
        if (user == null) {
            throw new SecurityException("Usuário ou senha inválidos");
        }
        
        if (!user.getEnabled()) {
            throw new SecurityException("Usuário desabilitado");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("Usuário ou senha inválidos");
        }
        
        return user;
    }
    
    public User findByUsername(String username) {
        return User.find("username", username).firstResult();
    }
}
