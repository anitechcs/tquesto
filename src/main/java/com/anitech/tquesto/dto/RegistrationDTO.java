package com.anitech.tquesto.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.util.Constants;

/**
 * A DTO representing a user, for registration
 * 
 * @author Tapas
 */
public class RegistrationDTO {
	
	public static final int PASSWORD_MIN_LENGTH = 3;
	public static final int PASSWORD_MAX_LENGTH = 20;
	
    @Pattern(regexp = Constants.USER_NAME_REGEX)
    @Size(min = 1, max = 50)
    private String userName;
    
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
    
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;
    
    @Size(max = 20)
    private String phone;

    private boolean activated = false;
    
    @Size(min = 2, max = 5)
    private String langKey;

    public RegistrationDTO() {
    }

    public RegistrationDTO(User user) {
        this(user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(),
            user.getEmail(),user.getPhone(), user.getActivated(), user.getLangKey());
    }

    public RegistrationDTO(String userName, String password, String firstName, String lastName,
        String email, String phone, boolean activated, String langKey) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.activated = activated;
        this.langKey = langKey;
    }

	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}

	public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
		return phone;
	}

	public boolean isActivated() {
        return activated;
    }
	
	public String getLangKey() {
        return langKey;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "userName='" + userName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            "}";
    }
}
