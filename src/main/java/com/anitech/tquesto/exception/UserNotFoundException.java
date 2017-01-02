package com.anitech.tquesto.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * This exception is thrown in case of a user not found
 * 
 * @author Tapas
 *
 */
public class UserNotFoundException extends UsernameNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String msg) {
		super(msg);
	}

}
