package com.caixa.invest.controller;

import com.caixa.invest.domain.User;
import com.caixa.invest.security.PasswordEncoder;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/debug")
public class DebugController {

    @Inject
    PasswordEncoder passwordEncoder;

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getUsers() {
        List<User> users = User.listAll();
        Map<String, Object> result = new HashMap<>();
        result.put("count", users.size());
        result.put("users", users.stream().map(u -> {
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", u.getUsername());
            userMap.put("email", u.getEmail());
            userMap.put("role", u.getRole().toString());
            userMap.put("enabled", String.valueOf(u.getEnabled()));
            userMap.put("passwordHash", u.getPassword().substring(0, 20) + "...");
            return userMap;
        }).toList());
        return result;
    }

    @GET
    @Path("/test-password")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> testPassword(@QueryParam("username") String username, @QueryParam("password") String password) {
        User user = User.find("username", username).firstResult();
        Map<String, Object> result = new HashMap<>();
        
        if (user == null) {
            result.put("error", "Usuário não encontrado");
            return result;
        }
        
        result.put("username", user.getUsername());
        result.put("storedHash", user.getPassword().substring(0, 30) + "...");
        result.put("passwordMatches", passwordEncoder.matches(password, user.getPassword()));
        result.put("enabled", user.getEnabled());
        
        return result;
    }

    @GET
    @Path("/generate-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> generateHash(@QueryParam("password") String password) {
        Map<String, String> result = new HashMap<>();
        result.put("password", password);
        result.put("hash", passwordEncoder.encode(password));
        return result;
    }
}
