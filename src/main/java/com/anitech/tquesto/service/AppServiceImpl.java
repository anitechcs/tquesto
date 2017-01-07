package com.anitech.tquesto.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anitech.tquesto.domain.App;
import com.anitech.tquesto.dto.AppDTO;
import com.anitech.tquesto.repository.AppRepository;
import com.anitech.tquesto.util.RandomUtil;

/**
 * App service implementation
 * 
 * @author Tapas
 *
 */
@Service
public class AppServiceImpl implements AppService {
	
	private final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);
	
	@Autowired
	private AppRepository appRepository;

	@Override
	public App createApp(AppDTO appDTO) {
		App app = new App();
		app.setAppName(appDTO.getAppName());
		app.setAppDescription(appDTO.getAppDescription());
		app.setAppIcon(appDTO.getAppIcon());
		app.setAppSecret(RandomUtil.generateAppSecretKey());
		app.setDefault(appDTO.isDefault());
		app.setEnableAndroidSdk(appDTO.isEnableAndroidSdk());
		app.setEnableIosSdk(appDTO.isEnableIosSdk());
		app.setEnableJavaSdk(appDTO.isEnableJavaSdk());
		app.setEnableJsSdk(appDTO.isEnableJsSdk());
		appRepository.save(app);
		return app;
	}

	@Override
	public App updateApp(AppDTO appDTO) {
		appRepository.findOneByAppId(appDTO.getAppId()).ifPresent(app -> {
			app.setAppName(appDTO.getAppName());
			app.setAppDescription(appDTO.getAppDescription());
			app.setAppIcon(appDTO.getAppIcon());
			app.setAppSecret(RandomUtil.generateAppSecretKey());
			app.setDefault(appDTO.isDefault());
			app.setEnableAndroidSdk(appDTO.isEnableAndroidSdk());
			app.setEnableIosSdk(appDTO.isEnableIosSdk());
			app.setEnableJavaSdk(appDTO.isEnableJavaSdk());
			app.setEnableJsSdk(appDTO.isEnableJsSdk());
            appRepository.save(app);
            logger.debug("Changed Information for App: {}", app);
        });
		return null;
	}

	@Override
	public void deleteApp(Long appId) {
		appRepository.findOneByAppId(appId).ifPresent(app -> {
			appRepository.delete(app);
			logger.debug("Deleted User: {}", app);
        });
	}

	@Override
	public App getAppById(Long appId) {
		return appRepository.findOne(appId);
	}
	
	@Override
	public Optional<App> findAppById(Long appId) {
		Optional<App> optionalApp = appRepository.findOneByAppId(appId);
		return optionalApp;
	}
	
	@Override
	public App getAppByName(String appName) {
		Optional<App> optionalApp = appRepository.findOneByAppName(appName);
        App app = null;
        if (optionalApp.isPresent()) {
        	app = optionalApp.get();
        }
		return app;
	}
	
	@Override
	public Optional<App> findAppByName(String appName) {
		Optional<App> optionalApp = appRepository.findOneByAppName(appName);
		return optionalApp;
	}

	@Override
	public List<App> getAllApps() {
		return appRepository.findAll();
	}

}
