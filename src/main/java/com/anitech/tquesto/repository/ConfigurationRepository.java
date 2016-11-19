package com.anitech.tquesto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anitech.tquesto.domain.Configuration;

/**
 * Configuration repository
 * 
 * @author Tapas
 *
 */
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
