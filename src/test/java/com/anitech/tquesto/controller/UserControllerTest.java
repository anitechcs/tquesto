package com.anitech.tquesto.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anitech.tquesto.repository.UserRepository;
import com.anitech.tquesto.service.MailService;
import com.anitech.tquesto.service.UserService;

/**
 * User Controller test
 * 
 * @author Tapas
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
    MockMvc mockMvc;
	
    @MockBean
    UserService userServiceMock;
    
    @MockBean
    MailService mailServiceMock;
    
    @MockBean
    UserRepository userRepositoryMock;
    
    @Test
    public void testUsersGET() throws Exception {
    	
    }
    
    @Test
    public void testUsersPOST() throws Exception {
    	
    }
    
    @Test
    public void testUsersPUT() throws Exception {
    	
    }
    
    @Test
    public void testUsersDELETE() throws Exception {
    	
    }
    
}
