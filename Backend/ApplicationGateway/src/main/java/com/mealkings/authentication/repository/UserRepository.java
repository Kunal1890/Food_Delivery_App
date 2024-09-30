package com.mealkings.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealkings.authentication.entity.Credentials;

@Repository
public interface UserRepository extends JpaRepository<Credentials, Long> {
	Optional<Credentials> findByEmail(String email);
	Optional<Credentials> findByUsername(String username);
}