package com.anitech.tquesto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anitech.tquesto.domain.Configuration;

/**
 * Configuration repository
 * 
 * @author Tapas
 *
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
