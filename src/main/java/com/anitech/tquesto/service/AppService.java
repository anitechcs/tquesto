package com.anitech.tquesto.service;

import java.util.List;
import java.util.Optional;

import com.anitech.tquesto.domain.App;
import com.anitech.tquesto.dto.AppDTO;

/**
 * App service interface
 * 
 * @author Tapas
 *
 */
public interface AppService {

	public App createApp(AppDTO appDTO);
	
	public App updateApp(AppDTO appDTO);
	
	public void deleteApp(Long appId);
	
	public App getAppById(Long appId);
	
	public App getAppByName(String appName);
	
	public List<App> getAllApps();

	public Optional<App> findAppByName(String appName);

	public Optional<App> findAppById(Long appId);
	
}
