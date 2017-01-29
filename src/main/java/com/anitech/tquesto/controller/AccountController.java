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
import com.anitech.tquesto.dto.RegistrationDTO;
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
     * @param registrationDTO
     * @param request
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the username or e-mail is already in use
     */
    @PostMapping(path = "/register")
    public ResponseEntity<Map<String, Object>> registerAccount(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
    	logger.debug("Inside registerAccount() method!");
    	HashMap<String, Object> responseMap = new HashMap<>();
    	
    	try {
    		Optional<User> user = userService.getUserByUserName(registrationDTO.getUserName().toLowerCase());
        	if(user.isPresent()) {
        		responseMap.put("statusCode", "400");
        		responseMap.put("errMsg", "Username already in use");
        		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        	}
        	
        	user = userService.getUserByEmail(registrationDTO.getEmail());
        	if(user.isPresent()) {
        		responseMap.put("statusCode", "400");
        		responseMap.put("errMsg", "Email address already in use");
        		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        	}
        	
        	User newUser = userService.createUser(registrationDTO.getUserName().toLowerCase().trim(), registrationDTO.getPassword(),
	        registrationDTO.getFirstName(), registrationDTO.getLastName(), registrationDTO.getEmail().toLowerCase(),
	        registrationDTO.getLangKey());
	        
	        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	        mailService.sendActivationEmail(newUser, baseUrl);
	        
	        responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
		} catch (Exception ex) {
			responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", ex.getMessage()); 
			logger.error("Exception occoured at registerAccount: " + ex);
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
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody String password) {
    	HashMap<String, Object> responseMap = new HashMap<>();
    	if (!checkPasswordLength(password)) {
    		responseMap.put("statusCode", "1200");
			responseMap.put("errMessage", "Incorrect password"); 
    		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        }
    	try {
			userService.changePassword(password);
			responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
		} catch (Exception e) {
			responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", "Password change has failed"); 
			logger.error("Exception occoured at changePassword: " + e);
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/init : Send an e-mail to reset the password of the user
     *
     * @param mail the mail of the user
     * @param request the HTTP request
     * @return the ResponseEntity with status 200 (OK) if the e-mail was sent, or status 400 (Bad Request) if the e-mail address is not registered
     */
    @PostMapping(path = "/account/reset_password/init")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
    	logger.debug("Password reset email: " + mail);
    	HashMap<String, Object> responseMap = new HashMap<>();
    	Optional<User> user = userService.requestPasswordReset(mail);
    	if(user.isPresent()) {
    		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            mailService.sendPasswordResetMail(user.get(), baseUrl);
    		responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
    	} else {
    		responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", "E-mail address not registered"); 
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
        return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset_password/finish")
    public ResponseEntity<Map<String, Object>> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
    	HashMap<String, Object> responseMap = new HashMap<>();
    	if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
    		responseMap.put("statusCode", "1200");
			responseMap.put("errMessage", "Incorrect password"); 
    		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.BAD_REQUEST);
        }
    	
    	Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
    	if(user.isPresent()) {
    		responseMap.put("statusCode", "0");
			responseMap.put("errMsg", "");
    	} else {
    		responseMap.put("statusCode", "1000");
			responseMap.put("errMessage", "Password reset has failed"); 
			return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    	return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatus.OK);
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= UserDTO.PASSWORD_MIN_LENGTH &&
            password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }
    
}
