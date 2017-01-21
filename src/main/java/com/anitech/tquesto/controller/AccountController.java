package com.anitech.tquesto.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.dto.KeyAndPasswordDTO;
import com.anitech.tquesto.dto.UserDTO;
import com.anitech.tquesto.service.MailService;
import com.anitech.tquesto.service.UserService;

/**
 * 
 * REST controller for managing the current user's account.
 * 
 * @author Tapas
 *
 */
@RestController
@RequestMapping("/api")
public class AccountController {

	private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;
    
    
    /**
     * POST  /register : register the user.
     *
     * @param userDTO
     * @param request
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the username or e-mail is already in use
     */
    @PostMapping(path = "/register")
    public ResponseEntity<Map<String, Object>> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
    	logger.debug("Inside registerAccount() method!");
    	HashMap<String, Object> responseMap = new HashMap<>();
    	
    	try {
    		Optional<User> user = userService.getUserByUserName(userDTO.getUserName().toLowerCase());
        	if(user.isPresent()) {
        		responseMap.put("statusCode", "400");
        		responseMap.put("errMsg", "Username already in use");
        		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        	}
        	
        	user = userService.getUserByEmail(userDTO.getEmail());
        	if(user.isPresent()) {
        		responseMap.put("statusCode", "400");
        		responseMap.put("errMsg", "Email address already in use");
        		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        	}
        	
        	User newUser = userService.createUser(userDTO.getUserName().toLowerCase().trim(), userDTO.getPassword(),
	        userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
	        userDTO.getLangKey());
	        
	        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	        mailService.sendActivationEmail(newUser, baseUrl);
	        
	        responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
		} catch (Exception ex) {
			responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", ex.getMessage()); 
			logger.error("Exception occoured at getEventDetails: " + ex);
			ex.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
        return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.CREATED);
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    public ResponseEntity<Map<String, Object>> activateAccount(@RequestParam(value = "key") String key) {
    	logger.debug("User activation key: " + key);
    	HashMap<String, Object> responseMap = new HashMap<>();
    	Optional<User> user = userService.activateRegistration(key);
    	if(user.isPresent()) {
    		responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
    	} else {
    		responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", "User activation failed"); 
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
    }
    
    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getCurrentUser())
            .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
    
    
    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = "/account/change_password", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an e-mail to reset the password of the user
     *
     * @param mail the mail of the user
     * @param request the HTTP request
     * @return the ResponseEntity with status 200 (OK) if the e-mail was sent, or status 400 (Bad Request) if the e-mail address is not registered
     */
    @PostMapping(path = "/account/reset_password/init", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        return userService.requestPasswordReset(mail)
            .map(user -> {
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
                mailService.sendPasswordResetMail(user, baseUrl);
                return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset_password/finish", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
              .map(user -> new ResponseEntity<String>(HttpStatus.OK))
              .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= UserDTO.PASSWORD_MIN_LENGTH &&
            password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }
    
}
