package com.anitech.tquesto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anitech.tquesto.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
	
}
