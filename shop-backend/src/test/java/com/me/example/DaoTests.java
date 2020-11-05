package com.me.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.me.example.cart.InCartProduct;
import com.me.example.cart.InCartProductService;
import com.me.example.exceptions.EntityDoesNotExistException;
import com.me.example.product.Product;
import com.me.example.product.ProductService;
import com.me.example.user.MyUser;
import com.me.example.user.MyUserService;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DaoTests {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private MyUserService myUserService;
	
	@Autowired
	private InCartProductService inCartProductService;

	
	@Test
	public void productService_saveTest() throws Exception {

		Product prod = createAndPersistTestProduct();

		assertNotNull(prod.getId());
		assertEquals(prod.getName(), "test product");


	}
	
	@Test
	public void productService_findAllTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		List<Product> list = productService.findAll();

		assertFalse(list.isEmpty());
	}
	
	@Test
	public void productService_findByIdTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		Product prod2 = productService.findById(prod.getId());

		assertEquals(prod2.getName(), prod.getName());
	}
	
	@Test
	public void productService_deleteByIdTest() throws Exception {
		Product prod = createAndPersistTestProduct();

		productService.delete(prod.getId());

		assertThrows(EntityDoesNotExistException.class, () -> {
			productService.findById(prod.getId());
			}
		);
	}
	
	@Test
	@DirtiesContext
	public void inCartProductService_saveTest() throws Exception {

		InCartProduct prod = createAndPersistTestInCartProductWithProduct();

		assertNotNull(prod.getId());
		assertEquals(prod.getAmount(), 1);


	}
	
	@Test
	public void inCartProductService_findAllTest() throws Exception {
		InCartProduct prod = createAndPersistTestInCartProductWithProduct();

		List<InCartProduct> list = inCartProductService.findAll();

		assertFalse(list.isEmpty());
	}
	
	@Test
	public void inCartProductService_findByIdTest() throws Exception {
		InCartProduct prod = createAndPersistTestInCartProductWithProduct();

		InCartProduct prod2 = inCartProductService.findById(prod.getId());

		assertEquals(prod2.getAmount(), prod.getAmount());
	}
	
	@Test
	public void inCartProductService_deleteByIdTest() throws Exception {
		InCartProduct prod = createAndPersistTestInCartProductWithProduct();

		inCartProductService.delete(prod.getId());

		assertThrows(EntityDoesNotExistException.class, () -> {
			inCartProductService.findById(prod.getId());
			}
		);
	}
	
	@Test
	@DirtiesContext
	public void myUserService_saveTest() throws Exception {

		MyUser prod = createAndPersistTestMyUserWithInCartProductWithProduct();

		assertNotNull(prod.getId());
		assertEquals(prod.getEmail(),"test@test.com");


	}
	
	@Test
	public void myUserService_findAllTest() throws Exception {
		MyUser prod = createAndPersistTestMyUserWithInCartProductWithProduct();

		List<MyUser> list = myUserService.findAll();

		assertFalse(list.isEmpty());
	}
	
	@Test
	public void myUserService_findByIdTest() throws Exception {
		MyUser prod = createAndPersistTestMyUserWithInCartProductWithProduct();

		MyUser prod2 = myUserService.findById(prod.getId());

		assertEquals(prod2.getEmail(), prod.getEmail());
	}
	

	
	private Product createAndPersistTestProduct() {
		Product prod = new Product("test product", 3.33, 10);
		prod.setCategories(Lists.newArrayList("Laptops"));

		prod = productService.save(prod);
		
		return prod;
	}
	
	private InCartProduct createAndPersistTestInCartProductWithProduct() {
		Product prod = new Product("test product", 3.33, 10);
		prod.setCategories(Lists.newArrayList("Laptops"));

		prod = productService.save(prod);
		
		InCartProduct incart = new InCartProduct(prod, (short) 1);
		incart = inCartProductService.save(incart);
		
		return incart;
	}
	
	private MyUser createAndPersistTestMyUserWithInCartProductWithProduct() {
		Product prod = new Product("test product" , 3.33, 10);
		prod.setCategories(Lists.newArrayList("Laptops"));

		prod = productService.save(prod);
		
		InCartProduct incart = new InCartProduct(prod, (short) 1);
		incart = inCartProductService.save(incart);
		
		MyUser user = new MyUser("test@test.com", "user", "");
		myUserService.save(user);
		
		return user;
	}
	


}
