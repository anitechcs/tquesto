package com.anitech.tquesto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anitech.tquesto.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 * 
 * @author Tapas
 * 
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
	
}
