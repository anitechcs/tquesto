package com.anitech.tquesto.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anitech.tquesto.auth.SecurityUtils;
import com.anitech.tquesto.domain.Authority;
import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.dto.UserDTO;
import com.anitech.tquesto.repository.AuthorityRepository;
import com.anitech.tquesto.repository.UserRepository;
import com.anitech.tquesto.util.Constants;
import com.anitech.tquesto.util.RandomUtil;

/**
 * User service implementation
 * 
 * @author Tapas
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;
    
    @Override
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    @Override
    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    @Override
    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    @Override
    public User createUser(String userName, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(Constants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUserName(userName);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        if (langKey == null) {
        	newUser.setLangKey("en"); // default language
        } else {
        	newUser.setLangKey(langKey);
        }
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    
    @Override
    public User createUser(UserDTO userArg) {
        User user = new User();
        user.setUserName(userArg.getUserName());
        user.setFirstName(userArg.getFirstName());
        user.setLastName(userArg.getLastName());
        user.setEmail(userArg.getEmail());
        user.setPhone(userArg.getPhone());
        if (userArg.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(userArg.getLangKey());
        }
        if (userArg.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userArg.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(false);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    @Override
    public void updateUser(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByUserName(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    @Override
    public void updateUser(Long id, String userName, String firstName, String lastName, String email,
        boolean activated, String langKey, Set<String> authorities) {

        Optional.of(userRepository
            .findOne(id))
            .ifPresent(u -> {
                u.setUserName(userName);
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setActivated(activated);
                u.setLangKey(langKey);
                Set<Authority> managedAuthorities = u.getAuthorities();
                managedAuthorities.clear();
                if(authorities != null) {
                	authorities.stream().forEach(
                        authority -> managedAuthorities.add(authorityRepository.findOne(authority))
                    );
                }
                log.debug("Changed Information for User: {}", u);
            });
    }

    @Override
    public void deleteUser(String userName) {
        userRepository.findOneByUserName(userName).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    @Override
    public void changePassword(String password) {
        userRepository.findOneByUserName(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmail(email).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Optional<User> optionalUser = userRepository.findOneByUserName(SecurityUtils.getCurrentUserLogin());
        User user = null;
        if (optionalUser.isPresent()) {
        	user = optionalUser.get();
            user.getAuthorities().size(); // eagerly load the association
        }
        return user;
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getUserName());
            userRepository.delete(user);
        }
    }

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAllWithAuthorities(pageable);
	}
    
}
