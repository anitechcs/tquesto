package com.anitech.tquesto;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tquesto Application Tests
 * 
 * @author Tapas
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TquestoApplicationTests {

	@MockBean
	ServletContext mockServletContext;
	
	@Test
	public void contextLoads() {
	}

}
