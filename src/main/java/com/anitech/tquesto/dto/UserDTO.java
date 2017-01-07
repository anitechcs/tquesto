package com.anitech.tquesto.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.anitech.tquesto.domain.Authority;
import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO representing a user, with his authorities.
 * 
 * @author Tapas
 */
public class UserDTO {
	
	public static final int PASSWORD_MIN_LENGTH = 3;
	public static final int PASSWORD_MAX_LENGTH = 20;
	
	private Long id;

    @Pattern(regexp = Constants.USER_NAME_REGEX)
    @Size(min = 1, max = 50)
    private String userName;
    
    @JsonIgnore
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

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    }

    public UserDTO(Long id, String userName, String password, String firstName, String lastName,
        String email, boolean activated, String langKey, Set<String> authorities) {
    	this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
    }


    public Long getId() {
		return id;
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

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "userName='" + userName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}
