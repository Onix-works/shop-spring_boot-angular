package com.me.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import com.google.common.collect.Lists;
import com.me.example.cart.InCartProduct;
import com.me.example.cart.InCartProductService;
import com.me.example.product.Image;
import com.me.example.product.ImageService;
import com.me.example.product.Product;
import com.me.example.product.ProductService;
import com.me.example.security.oauth2.GithubUserService;
import com.me.example.user.MyUser;
import com.me.example.user.MyUserService;

@SpringBootApplication
public class ShopBackendApplication {
	
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
	
	Logger logger = LoggerFactory.getLogger(ShopBackendApplication.class);
	
	/**test data
	 */
	@PostConstruct
    public void init() {
		{      
		      Product prod1 = new Product("Acer Spin 5 SP513-53N-76ZK Laptop", 999.99, 14);
		      Product prod2 = new Product("Microsoft Modern Mobile Mouse", 39.99, 58);
		      Product prod3 = new Product("MSI Trident X Plus 9SE-062US Gaming PC", 2499.99, 7);
		      Product prod4 = new Product("iBUYPOWER Trace PRO9500 Gaming Desktop", 1149.99, 10);
		      Product prod5 = new Product("Acer B247Y 23.8\" Monitor", 199.99, 17);
		      Product prod6 = new Product("Samsung 27\" Odyssey G7 Gaming Monitor", 699.99, 21);
		      Product prod7 = new Product("Jabra Evolve2 85", 449.99, 16);
		      Product prod8 = new Product("Surface Laptop 3", 599.99, 4);
		      
		      prod1.setCategories(Lists.newArrayList("Laptops")); 
		      prod2.setCategories(Lists.newArrayList("Accessories"));
		      prod3.setCategories(Lists.newArrayList("PC"));
		      prod4.setCategories(Lists.newArrayList("PC"));
		      prod5.setCategories(Lists.newArrayList("Monitors"));
		      prod6.setCategories(Lists.newArrayList("Monitors"));
		      prod7.setCategories(Lists.newArrayList("Accessories"));
		      prod8.setCategories(Lists.newArrayList("Laptops"));
		     
		      fillInfoFromFile(prod1, "acer_spin");
		      fillInfoFromFile(prod2, "modern_mouse");
		      fillInfoFromFile(prod3, "msi_trident");
		      fillInfoFromFile(prod4, "ibuypower");
		      fillInfoFromFile(prod5, "acer_b247y");
		      fillInfoFromFile(prod6, "odyssey_g7");
		      fillInfoFromFile(prod7, "jabra_evolve");
		      fillInfoFromFile(prod8, "surface_3");
  
		      MyUser myUser = new MyUser( "me@me.com", "user" , "/images/laptop-user.png");
		      myUser = myUserService.save(myUser);
		      
		      InCartProduct inCart1 = new InCartProduct(prod1, (short) 2);
		      InCartProduct inCart2 = new InCartProduct(prod2, (short) 14);
		      inCart1 = inCartProductService.save(inCart1);
		      inCart2 = inCartProductService.save(inCart2);

		      myUser.addInCartProduct(inCart1);
		      myUser.addInCartProduct(inCart2);
		      myUserService.update(myUser);
		      	
		      }
    }
    
    public static void main(String[] args) {
      SpringApplication.run(ShopBackendApplication.class, args);
   }
    
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer()
    {
        return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(Product.class);
            config.exposeIdsFor(Image.class);
        });
    }
    /**
     * inserts info into Product from files with given name pattern
     * @param prod
     * @param filename
     */
    private void fillInfoFromFile(Product prod, String filename) {
		  InputStream inputStream = getClass().getClassLoader()
				  .getResourceAsStream("products/" + filename + ".properties");
	      Properties props = new Properties();
	      try {
	      props.load(inputStream);
	      }
	      catch(IOException e){
	    	  e.printStackTrace();
	      }
	      Map<String, String> map = prod.getQualities();
	      for (String key : props.stringPropertyNames()) {
	    	    String value = props.getProperty(key);
	    	    map.put(key, value);
	    	}
	      
	      inputStream = getClass().getClassLoader()
	    		  .getResourceAsStream("products/" + filename + ".desc");
	      String result = new BufferedReader(new InputStreamReader(inputStream))
	    		  .lines().collect(Collectors.joining("\n"));
	      prod.setDescription(result);
	      	     
	      Image image = new Image("/images/" + filename + ".png");
	      image = imageService.save(image);
	      prod.setImage(image);
	      prod = productService.save(prod);
	}

}

