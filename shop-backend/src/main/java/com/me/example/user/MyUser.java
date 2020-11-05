package com.me.example.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import com.me.example.product.Image;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper=true)
@Data
public class MyUser extends User{
	
	public MyUser(){
	}
	
	public MyUser(String email, String password, String imageUrl){
		this.email = email;
		this.password = password;
		this.image = new Image(imageUrl);
	}
	
	@ValidEmail
	@NonNull
	private String email;
	
	@NonNull
	private String password;

	@ElementCollection
	private List<String> roles;
		
	@Override
	public UserDTO toDTO() { 
		return new UserDTO(this.getId(), this.getEmail(), this.getInCartProducts(), this.getImage().getId());
	}
	

}
