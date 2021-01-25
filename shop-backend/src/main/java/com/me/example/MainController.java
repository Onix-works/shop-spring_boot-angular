package com.me.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.me.example.cart.InCartProduct;
import com.me.example.cart.InCartProductService;
import com.me.example.product.Image;
import com.me.example.product.ImageService;
import com.me.example.product.ProductService;
import com.me.example.security.oauth2.GithubUser;
import com.me.example.security.oauth2.GithubUserService;
import com.me.example.user.MyUser;
import com.me.example.user.MyUserService;
import com.me.example.user.UserDTO;

@RestController
public class MainController {
	
	Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	MyUserService myUserService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	InCartProductService inCartProductService;
	
	@Autowired
	GithubUserService githubUserService;
	
	
  
    @GetMapping("/api/user")
    public Principal principal(Principal  principal) {
    	return principal;
    }
  
    @PostMapping("/api/register")
    public MyUser register(@RequestBody MyUser  user) {
    	user = myUserService.save(user);
    	return user;
    }
    
    @PreAuthorize("isAuthenticated()") 
    @GetMapping("/api/user/{name}")
    public UserDTO getUser(@PathVariable String name) {
    	if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
    		MyUser user = myUserService.findByEmail(name);
	    	UserDTO userdto = user.toDTO();
	    	return userdto;   	
    	}
    	else {
    		if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken){
    			GithubUser user = githubUserService.findByLogin(name);
    	    	UserDTO userdto = user.toDTO();
    	    	return userdto;
    		}
    		else
    			return null;
    	}
    }
    
    @PreAuthorize("isAuthenticated()") 
    @PostMapping("/api/user/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
    	if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
    		MyUser myuser = myUserService.findById(user.getId());
    		List<InCartProduct> oldList = myuser.getInCartProducts();
    		myuser.fromDTO(user);
    		updateProductInStock(oldList, myuser.getInCartProducts());
    		myuser = myUserService.update(myuser);
    		user = myuser.toDTO();
	    	return user;   	
    	}
    	else {
    		if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken){
    			GithubUser gituser = githubUserService.findById(user.getId());
    			
    			List<InCartProduct> oldList = gituser.getInCartProducts();
    			if (!oldList.isEmpty())
    			logger.info("OLD LIST" + oldList.get(0).getAmount());
    	    	gituser.fromDTO(user);
    	    	updateProductInStock(oldList, gituser.getInCartProducts());
    	    	gituser = githubUserService.update(gituser);
    	    	user = gituser.toDTO();
    	    	return user;
    		}
    		else
    			throw new RuntimeException("Not authorized to update");
    	}
    }
    
    @GetMapping(value = "api/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImageWithMediaType(@PathVariable long id) throws IOException {
    	File file = new ClassPathResource(imageService.findById(id).getSource()).getFile();
    	byte[] fileContent = Files.readAllBytes(file.toPath());
        return fileContent;
    }
    
    @PreAuthorize("isAuthenticated()") 
    @GetMapping(value = "api/userphoto/{id}")
    public void getUserImageWithMediaType(@PathVariable long id, HttpServletResponse response) throws IOException {
    	if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
    		response.sendRedirect("http://localhost:8080/api/image/" + id);
    		return;
    	}
    	else {
    		if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken){
    			Image image = imageService.findById(id);
    			response.sendRedirect(image.getSource());
    	    	return;
    		}
    		else
    			return;
    	}
    }
    
    /**
     * recalculates products in stock depending on products currently in cart
     * @param oldList
     * @param newList
     */
    private void updateProductInStock(List<InCartProduct> oldList,
    		List<InCartProduct> newList) {
    	boolean productPresent = false;
    	for (InCartProduct incartnew : newList) {
    		for (InCartProduct incartold : oldList) {
    			if (incartold.getProduct().getId() == incartnew.getProduct().getId()) {
    				logger.info("FOUND IN OLD LIST" + incartnew.getProduct().getName() +
    						"WAS INSTOCK" + incartnew.getProduct().getInStock());
    				incartnew.getProduct().setInStock(incartnew.getProduct().getInStock() - incartnew.getAmount() + incartold.getAmount());
    				logger.info("NOW INSTOCK" + incartnew.getProduct().getInStock());
    				productPresent = true;
    			}
    		}
    		if (!productPresent) {
    			logger.info("NOT FOUND IN OLD LIST" + incartnew.getProduct().getName() +
						"WAS INSTOCK" + incartnew.getProduct().getInStock());
    			incartnew.getProduct().setInStock(incartnew.getProduct().getInStock() - incartnew.getAmount());
    			logger.info("NOW INSTOCK" + incartnew.getProduct().getInStock());
    		}
    		productPresent = false;
    	}
    	
    	for (InCartProduct incartold : oldList) {
    		for (InCartProduct incartnew : newList) {
    			if (incartold.getProduct().getId() == incartnew.getProduct().getId()) {
    				logger.info("FOUND IN NEW LIST" + incartold.getProduct().getName() +
    						"WAS INSTOCK" + incartold.getProduct().getInStock());
    				productPresent = true;
    			}
    		}
    		if (!productPresent) {
    			logger.info("NOT FOUND IN NEW LIST" + incartold.getProduct().getName() +
						"WAS INSTOCK" + incartold.getProduct().getInStock());
    			incartold.getProduct().setInStock(incartold.getProduct().getInStock() + incartold.getAmount());
    			logger.info("NOW INSTOCK" + incartold.getProduct().getInStock());
    			productService.update(incartold.getProduct());
			}
    		
    	}
    	}
}
