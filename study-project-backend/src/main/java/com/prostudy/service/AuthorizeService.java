package com.prostudy.service;

import com.prostudy.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    String sendValidateEmail(String email, String sessionId);

    String validateAndRegister(String username, String password, String email, String code,String sessionId);
}
