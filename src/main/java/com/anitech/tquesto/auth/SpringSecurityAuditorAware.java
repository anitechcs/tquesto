package com.anitech.tquesto.auth;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.anitech.tquesto.util.Constants;

/**
 * Implementation of AuditorAware based on Spring Security for Entity Auditing
 * 
 * @author Tapas
 * 
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return (userName != null ? Optional.of(userName) : Optional.of(Constants.SYSTEM_ACCOUNT));
    }
    
}
