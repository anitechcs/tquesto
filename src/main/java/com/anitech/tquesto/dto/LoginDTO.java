package com.anitech.tquesto.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.anitech.tquesto.util.Constants;

/**
 * @author Tapas
 *
 */
public class LoginDTO {

	@Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = UserDTO.PASSWORD_MIN_LENGTH, max = UserDTO.PASSWORD_MAX_LENGTH)
    private String password;

    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "password='*****'" +
            ", username='" + username + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
    
}
