package com.anitech.tquesto.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

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

import com.anitech.tquesto.domain.App;

/**
 * App integration test
 * 
 * @author Tapas
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppIntegrationTest {

	@Autowired
    private TestRestTemplate restTemplate;
	
	private static App testApp;
	
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
    public void createApp() {
    	App app = new App();
    	app.setAppName("TestApp");
    	app.setAppDescription("TestApp Description");
    	app.setDefault(true);
    	app.setEnableAndroidSdk(false);
    	app.setEnableIosSdk(false);
    	app.setEnableJavaSdk(false);
    	app.setEnableJsSdk(false);
    	
    	HttpEntity<App> entity = new HttpEntity<App>(app, createHeaders("user", "user"));
        ResponseEntity<App> responseEntity = restTemplate.postForEntity("/api/apps", entity, App.class);
        App createdApp = responseEntity.getBody();
        testApp = createdApp;
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("TestApp", createdApp.getAppName());
    }
    
    @Test
    public void getApp() throws Exception {
    	HttpEntity<App> entity = new HttpEntity<App>(createHeaders("user", "user"));
    	ResponseEntity<App> responseEntity = restTemplate.exchange("/api/apps/"+testApp.getAppId(), HttpMethod.GET, entity, App.class);
        App app = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("TestApp", app.getAppName());
    }
    
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public void getAllApps() throws Exception {
    	HttpEntity<App> entity = new HttpEntity<App>(createHeaders("user", "user"));
        ResponseEntity<List> responseEntity = restTemplate.exchange("/api/apps", HttpMethod.GET, entity, List.class);
        List<App> apps = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(apps.size()).isGreaterThan(0);
    }
    
    @Test
    public void updateApp() {
    	App app = testApp;
    	app.setAppName("UpdatedTestApp");
    	app.setAppDescription("Updated TestApp Description");
    	app.setDefault(false);
    	app.setEnableAndroidSdk(true);
    	app.setEnableIosSdk(false);
    	app.setEnableJavaSdk(false);
    	app.setEnableJsSdk(false);
    	
    	HttpEntity<App> entity = new HttpEntity<App>(app, createHeaders("user", "user"));
    	ResponseEntity<App> responseEntity = restTemplate.exchange("/api/apps", HttpMethod.PUT, entity, App.class);
    	App updatedApp = responseEntity.getBody();
    	assertEquals("UpdatedTestApp", updatedApp.getAppName());
    }
    
    @Test
    public void zDeleteApp() {
    	HttpEntity<App> entity = new HttpEntity<App>(createHeaders("user", "user"));
    	ResponseEntity<App> responseEntity = restTemplate.exchange("/api/apps/"+testApp.getAppId(), HttpMethod.DELETE, entity, App.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
}
