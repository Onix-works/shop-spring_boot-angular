package com.me.example.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	GithubUserService githubUserService;
	
	Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		
		OAuth2User oauthUser = super.loadUser(userRequest);
		
		String login = oauthUser.getAttributes().get("login").toString();
		String avatar_url = oauthUser.getAttributes().get("avatar_url").toString();

		if (githubUserService.loginExists(login)) {
			
			GithubUser user = githubUserService.findByLogin(login);
			user.getImage().setSource(avatar_url);
			githubUserService.update(user);
		}
		else {
			GithubUser user = new GithubUser(login, avatar_url);
			githubUserService.save(user);
		}
		logger.info(oauthUser.getAttributes().toString());
		
		return oauthUser;
		
	}
}
