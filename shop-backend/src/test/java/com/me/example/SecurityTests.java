package com.me.example;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class SecurityTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void unprotectedMethodNoTAuthenticatedTest() throws Exception {

		mockMvc.perform(get("/api/image/1")
			       .accept(MediaType.IMAGE_PNG_VALUE)
			       .contentType(MediaType.IMAGE_PNG_VALUE))
				.andExpect(status().isOk())
				.andExpect(unauthenticated());
	}
	

	@Test
	public void protectedMethodNoTAuthenticatedTest() throws Exception {

		mockMvc.perform(get("/api/userphoto/1"))
		    .andExpect(status().isUnauthorized())
			.andExpect(unauthenticated());
			
	}
	
	@Test
	@WithMockUser(username = "me@me.ru", password = "user")
	public void protectedMethodAuthenticatedTest() throws Exception {
		
		mockMvc.perform(get("/api/userphoto/1"))
	    .andExpect(status().isFound())
		.andExpect(authenticated());
		 
	}
	
	@Test
	public void loginWrongCredentialsTest() throws Exception {

		this.mockMvc.perform(formLogin().user("wrong@me.ru").password("user")		
				)
		.andExpect(status().isUnauthorized())
		.andExpect(unauthenticated());
	
	}
	
	@Test
	public void loginNoTValidCredentialsTest() throws Exception {

		this.mockMvc.perform(formLogin().user("wrong").password("wrong")		
				)
		.andExpect(status().isUnauthorized())
		.andExpect(unauthenticated());
	
	}
	
	@Test
	public void loginCorectCredentialsTest() throws Exception {

		this.mockMvc.perform(get("/api/userphoto/1").with(httpBasic("me@me.com","user"))		
				)
		.andExpect(status().isFound())
		.andExpect(authenticated());
	
	}
	

}
