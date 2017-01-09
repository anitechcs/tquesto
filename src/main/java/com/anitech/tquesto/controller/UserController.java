package com.anitech.tquesto.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.dto.UserDTO;
import com.anitech.tquesto.repository.UserRepository;
import com.anitech.tquesto.service.MailService;
import com.anitech.tquesto.service.UserService;
import com.anitech.tquesto.util.Constants;
import com.anitech.tquesto.util.HeaderUtil;
import com.anitech.tquesto.util.PaginationUtil;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 *
 * @author Tapas
 *
 */
@RestController
@RequestMapping("/api")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;
    

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the username and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param userDTO the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the username or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Secured(Constants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO, HttpServletRequest request) throws URISyntaxException {
        logger.debug("REST request to save User : {}", userDTO);
        //Lowercase the user username before comparing with database
        if (userRepository.findOneByUserName(userDTO.getUserName().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Username already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(userDTO);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            mailService.sendUserCreationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getUserName()))
                .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getUserName(), newUser.getUserName()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the username or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    @Secured(Constants.ADMIN)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        logger.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByUserName(userDTO.getUserName().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Username already in use")).body(null);
        }
        userService.updateUser(userDTO.getId(), userDTO.getUserName(), userDTO.getFirstName(),
            userDTO.getLastName(), userDTO.getEmail(), userDTO.isActivated(),
            userDTO.getLangKey(), userDTO.getAuthorities());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("A user is updated with identifier " + userDTO.getUserName(), userDTO.getUserName()))
            .body(new UserDTO(userService.getUserWithAuthorities(userDTO.getId())));
    }

    /**
     * GET  /users : get all users.
     * 
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAllWithAuthorities(pageable);
        List<UserDTO> userDTOs = page.getContent().stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(userDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:userName : get the user.
     *
     * @param userName of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{userName:" + Constants.USER_NAME_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userName) {
        logger.debug("REST request to get User : {}", userName);
        return userService.getUserWithAuthoritiesByUserName(userName)
                .map(UserDTO::new)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /users/:userName : delete the User.
     *
     * @param userName of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{userName:" + Constants.USER_NAME_REGEX + "}")
    @Secured(Constants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String userName) {
        logger.debug("REST request to delete User: {}", userName);
        userService.deleteUser(userName);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + userName, userName)).build();
    }

    /**
     * SEARCH  /_search/users/:query : search for the User corresponding
     * to the query.
     *
     * @param query the query to search
     * @return the result of the search
     */
    @GetMapping("/_search/users/{query}")
    public List<User> search(@PathVariable String query) {
        return null;
        
        /*StreamSupport
            .stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());*/
    }
    
}
