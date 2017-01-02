package com.anitech.tquesto.auth;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.exception.UserNotActivatedException;
import com.anitech.tquesto.exception.UserNotFoundException;
import com.anitech.tquesto.repository.UserRepository;

/**
 * Spring user details service implementation
 * 
 * @author Tapas
 *
 */
@Component
public class CustomUserDetailService implements UserDetailsService {

	private final Logger LOG = LoggerFactory.getLogger(CustomUserDetailService.class);
	
	@Inject
    private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		LOG.debug("Inside AuthenticationUserDetailService->loadUserByUsername() method!");
		String lowercaseLogin = userName.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneByUserName(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.getActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                user.getPassword(),
                grantedAuthorities);
        }).orElseThrow(() -> new UserNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
	}

}
