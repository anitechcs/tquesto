package com.anitech.tquesto.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.dto.UserDTO;

/**
 * User service interface
 * 
 * @author Tapas
 *
 */
public interface UserService {

	public Optional<User> activateRegistration(String key);

	public Optional<User> completePasswordReset(String newPassword, String key);

	public Optional<User> requestPasswordReset(String mail);

	public User createUser(UserDTO userDTOArg);
	
	public User createUser(String userName, String password, String firstName, String lastName, String email, String langKey);

	public void updateUser(String firstName, String lastName, String email, String langKey);

	public void updateUser(Long id, String userName, String firstName, String lastName, String email, boolean activated,
			String langKey, Set<String> authorities);

	public void deleteUser(String userName);

	public void changePassword(String password);

	public Optional<User> getUserByUserName(String userName);
	
	public Optional<User> getUserByEmail(String email);

	public User getUser(Long id);

	public User getCurrentUser();

	public void removeNotActivatedUsers();

	public List<User> getAllUsers();
	
	public Page<User> getAllUsers(Pageable pageable);

}
