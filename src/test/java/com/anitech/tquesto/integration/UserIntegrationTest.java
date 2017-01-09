package com.anitech.tquesto.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.anitech.tquesto.domain.Authority;
import com.anitech.tquesto.domain.User;
import com.anitech.tquesto.util.Constants;

/**
 * User integration tests
 * 
 * @author Tapas
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationTest {

	@Autowired
    private TestRestTemplate restTemplate;
	
	private static User testUser;
	
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 1L;
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}
	
    @Test
    public void createUser() {
    	User user = new User();
    	user.setFirstName("Test");
    	user.setLastName("User");
    	user.setEmail("test@user.com");
    	user.setPhone("9876234567");
    	user.setUserName("testuser");
    	Set<Authority> authorities = new HashSet<>();
    	authorities.add(new Authority(Constants.USER));
    	user.setAuthorities(authorities);
    	
    	HttpEntity<User> entity = new HttpEntity<User>(user, createHeaders("user", "user"));
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("/api/users", entity, User.class);
        User createdUser = responseEntity.getBody();
        testUser = createdUser;
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Test", createdUser.getFirstName());
        assertEquals("User", createdUser.getLastName());
        assertEquals("testuser", createdUser.getUserName());
    }
    
    @Test
    public void getUser() throws Exception {
    	HttpEntity<User> entity = new HttpEntity<User>(createHeaders("user", "user"));
    	ResponseEntity<User> responseEntity = restTemplate.exchange("/api/users/"+testUser.getUserName(), HttpMethod.GET, entity, User.class);
    	User user = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("testuser", user.getUserName());
    }
    
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public void getAllUsers() throws Exception {
    	HttpEntity<User> entity = new HttpEntity<User>(createHeaders("user", "user"));
        ResponseEntity<List> responseEntity = restTemplate.exchange("/api/users", HttpMethod.GET, entity, List.class);
        List<User> users = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(users.size()).isGreaterThan(0);
    }
    
    @Test
    public void updateUser() {
    	User user = testUser;
    	user.setFirstName("UpdatedTest");
    	user.setLastName("UpdatedUser");
    	System.out.println("Tapas:"+user.getAuthorities());
    	
    	HttpEntity<User> entity = new HttpEntity<User>(user, createHeaders("user", "user"));
    	ResponseEntity<User> responseEntity = restTemplate.exchange("/api/users", HttpMethod.PUT, entity, User.class);
    	User updatedUser = responseEntity.getBody();
    	assertEquals("UpdatedTest", updatedUser.getFirstName());
        assertEquals("UpdatedUser", updatedUser.getLastName());
    }
    
    @Test
    public void zDeleteUser() {
    	HttpEntity<User> entity = new HttpEntity<User>(createHeaders("user", "user"));
    	ResponseEntity<User> responseEntity = restTemplate.exchange("/api/users/"+testUser.getUserName(), HttpMethod.DELETE, entity, User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
	
}
