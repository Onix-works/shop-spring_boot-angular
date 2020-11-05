package com.me.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.example.cart.InCartProduct;
import com.me.example.user.MyUser;
import com.me.example.user.MyUserService;
import com.me.example.user.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class ControllerTests {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
 
	@Autowired
	private MainController controller; 
	
	@Autowired
	private MyUserService myUserService;
	
	
	@Test
	void contextLoads() {
		assertNotNull(controller);
	}
	
	@Test
	@WithMockUser(username = "me@me.com",password = "user")
	public void mainController_listTest() throws Exception {

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/resource")
			      .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResult = mvcResult.getResponse().getContentAsString();
		HashMap<String,Object> content = objectMapper.readValue(jsonResult, HashMap.class);
		assertTrue(content.size() > 0);
	}
	
	@Test
	public void mainController_registerTest() throws Exception {
		
		MyUser user = new MyUser();
		user.setEmail("test@test.com");
		user.setPassword("pass");
		String json = objectMapper.writeValueAsString(user);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
			      .accept(MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN,MediaType.ALL)
			      .contentType(MediaType.APPLICATION_JSON)
			      .content(json))
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResult = mvcResult.getResponse().getContentAsString();
		Map<String, Object> jsonObject = new JacksonJsonParser().parseMap(jsonResult);
		String principal =  (String) jsonObject.get("email");
		assertTrue(principal.equals("test@test.com"));
		
	}
	
	@Test
	@WithMockUser(username = "me@me.com",password = "user")
	public void mainController_getUserTest() throws Exception {

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/me@me.com")
			      .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResult = mvcResult.getResponse().getContentAsString();
		Map<String, Object> jsonObject = new JacksonJsonParser().parseMap(jsonResult);
		String name =  (String) jsonObject.get("name");
		assertTrue(name.equals("me@me.com"));
		
	}
	
	@Test
	@WithMockUser(username = "me@me.com",password = "user")
	public void mainController_updateUserTest() throws Exception {
		
		MyUser user = myUserService.findById((long) 1);
		assertFalse(user.getInCartProducts().isEmpty());
		List<InCartProduct> list= new ArrayList<InCartProduct>();
		
		UserDTO userdto = new UserDTO( (long) 1, "testname", list, null);
		String json = objectMapper.writeValueAsString(userdto);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/1")
			      .accept(MediaType.APPLICATION_JSON)
			      .contentType(MediaType.APPLICATION_JSON)
			      .content(json))
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResult = mvcResult.getResponse().getContentAsString();
		UserDTO receiveddto = objectMapper.readValue(jsonResult, UserDTO.class);
		assertTrue(receiveddto.getInCartProducts().isEmpty());
		
	}
	
	@Test
	public void mainController_getImageWithMediaTypeTest() throws Exception {

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/image/1")
			      .accept(MediaType.IMAGE_PNG_VALUE)
			      .contentType(MediaType.IMAGE_PNG_VALUE))
				.andExpect(status().isOk())
				.andReturn();
		
		byte[] result = mvcResult.getResponse().getContentAsByteArray();
		assertFalse(result.length == 0);
		
	}
	
	@Test
	@WithMockUser(username = "me@me.com",password = "user")
	public void mainController_getUserImageWithMediaTypeTest() throws Exception {

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/userphoto/1"))
				.andExpect(status().isFound())
				.andReturn();
		assertEquals(mvcResult.getResponse().getRedirectedUrl(), "http://localhost:8080/api/image/1");
		
	}
	
	
	
	

}
