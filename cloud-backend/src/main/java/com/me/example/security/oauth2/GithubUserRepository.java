package com.me.example.security.oauth2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.example.user.MyUser;

public interface GithubUserRepository extends JpaRepository<GithubUser, Long> {
	
	Optional<GithubUser> findByLogin(String login);
	
	@Override
    void delete(GithubUser user);

}