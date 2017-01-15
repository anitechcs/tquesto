package com.anitech.tquesto.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.anitech.tquesto.domain.App;
import com.anitech.tquesto.dto.AppDTO;
import com.anitech.tquesto.service.AppService;
import com.anitech.tquesto.service.MailService;
import com.anitech.tquesto.service.UserService;
import com.anitech.tquesto.util.Constants;
import com.anitech.tquesto.util.HeaderUtil;

/**
 * App controller
 * 
 * @author Tapas
 *
 */
@RestController
@RequestMapping("/api")
public class AppController {

	private final Logger logger = LoggerFactory.getLogger(AppController.class);
	
	@Inject
    private AppService appService;
	
	@Inject
    private UserService userService;
	
	@Inject
    private MailService mailService;
	
	
	/**
     * POST  /apps  : Creates a new App.
     * <p>
     * Creates a new app if the appName is not already used, and sends an
     * mail with access details.
     * </p>
     *
     * @param appDTO the user to app
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new app, or with status 400 (Bad Request) if app name is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
	@PostMapping("/apps")
    @Secured(Constants.ADMIN)
    public ResponseEntity<?> createApp(@RequestBody AppDTO appDTO, HttpServletRequest request) throws URISyntaxException {
		logger.debug("REST request to save App : {}", appDTO);
        if (appService.findAppByName(appDTO.getAppName()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("AppManagement", "appexists", "App name already in use"))
                .body(null);
        } else {
            App newApp = appService.createApp(appDTO);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            mailService.sendAppCreationEmail(userService.getCurrentUser(), newApp, baseUrl);
            return ResponseEntity.created(new URI("/api/apps/" + newApp.getAppName()))
                .headers(HeaderUtil.createAlert( "A App is created with identifier " + newApp.getAppName(), newApp.getAppName()))
                .body(newApp);
        }
	}
	
	/**
     * PUT  /apps : Updates an existing App.
     *
     * @param appDTO the app to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated app,
     * or with status 500 (Internal Server Error) if the app couldn't be updated
     */
	@PutMapping("/apps")
    @Secured(Constants.ADMIN)
    public ResponseEntity<AppDTO> updateApp(@RequestBody AppDTO appDTO) {
		logger.debug("REST request to update App : {}", appDTO);
        appService.updateApp(appDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("A App is updated with identifier " + appDTO.getAppName(), appDTO.getAppName()))
            .body(new AppDTO(appService.getAppById(appDTO.getAppId())));
	}
	
	/**
     * GET  /apps : get all apps.
     * 
     * @return the ResponseEntity with status 200 (OK) and with body all apps
     */
	@GetMapping("/apps")
    public ResponseEntity<List<App>> getAllApps() {
		List<App> apps = appService.getAllApps();
        return new ResponseEntity<>(apps, HttpStatus.OK);
	}
	
	/**
     * GET  /apps/:appId : get the app.
     *
     * @param appId of the app to find
     * @return the ResponseEntity with status 200 (OK) and with body the app, or with status 404 (Not Found)
     */
	@GetMapping("/apps/{appId}")
    public ResponseEntity<AppDTO> getApp(@PathVariable Long appId) {
		logger.debug("REST request to get App : {}", appId);
        return appService.findAppById(appId)
        		.map(AppDTO::new)
                .map(appDTO -> new ResponseEntity<>(appDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	/**
     * DELETE /apps/:appId : delete the App.
     *
     * @param appId of the app to delete
     * @return the ResponseEntity with status 200 (OK)
     */
	@DeleteMapping("/apps/{appId}")
    @Secured(Constants.ADMIN)
    public ResponseEntity<Void> deleteApp(@PathVariable Long appId) {
		logger.debug("REST request to delete App: {}", appId);
		appService.deleteApp(appId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A app is deleted with identifier " + appId, appId+"")).build();
	}
	
}
