package com.me.example.security.oauth2;

import javax.persistence.Entity;

import com.me.example.product.Image;
import com.me.example.user.User;
import com.me.example.user.UserDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class GithubUser extends User{
	
	GithubUser(String login, String avatar_url){
		this.login = login;
		this.image = new Image(avatar_url);
	}
	
	GithubUser(String login){
		this.login = login;
	}
	
	@NonNull
	private String login;
	
	public UserDTO toDTO() { 
		return new UserDTO(this.getId(), this.getLogin(), this.getInCartProducts(), this.getImage().getId());
	}
	
}