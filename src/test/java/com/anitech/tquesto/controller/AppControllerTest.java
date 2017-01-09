package com.anitech.tquesto.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anitech.tquesto.service.AppService;
import com.anitech.tquesto.service.MailService;
import com.anitech.tquesto.service.UserService;

/**
 * AppControllerTest tests
 * 
 * @author Tapas
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class AppControllerTest {

	@Autowired
    MockMvc mockMvc;
	
    @MockBean
    AppService appServiceMock;
    
    @MockBean
    UserService userServiceMock;
    
    @MockBean
    MailService mailServiceMock;
    
    @Test
    public void testAppsGET() throws Exception {
    	
    }
    
    @Test
    public void testAppsPOST() throws Exception {
    	
    }
    
    @Test
    public void testAppsPUT() throws Exception {
    	
    }
    
    @Test
    public void testAppsDELETE() throws Exception {
    	
    }
    
}
