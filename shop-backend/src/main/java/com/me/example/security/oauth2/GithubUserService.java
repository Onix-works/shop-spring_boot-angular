package com.me.example.security.oauth2;

import java.util.List;

public interface GithubUserService {
	List<GithubUser> findAll();
	GithubUser findById(Long id);
	GithubUser findByLogin(String login);
	GithubUser save(GithubUser user);
	GithubUser update(GithubUser user);
	boolean loginExists(String login);
	boolean idExists(Long id);

}