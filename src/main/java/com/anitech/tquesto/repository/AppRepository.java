package com.anitech.tquesto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anitech.tquesto.domain.App;

/**
 * Spring Data JPA repository for the App entity
 * 
 * @author Tapas
 *
 */
@Repository
public interface AppRepository extends JpaRepository<App, Long> {

	Optional<App> findOneByAppId(Long appId);
	
	Optional<App> findOneByAppName(String appName);
	
}
