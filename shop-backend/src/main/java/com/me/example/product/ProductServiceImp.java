package com.me.example.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.me.example.exceptions.EntityAlreadyExistException;
import com.me.example.exceptions.EntityDoesNotExistException;

@Service("productService")
public class ProductServiceImp implements ProductService {
	
	@Autowired
	ProductRepository repository;


	@Override
	public List<Product> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Product findById(Long id) {
		if (repository.findById(id).isPresent()){   
			return repository.findById(id).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No product with such id.");
		 }
	}

	@Override
	public Product findByName(String name) {
		if (repository.findByName(name).isPresent()){   
			return repository.findByName(name).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No product with such name.");
		 }
	}

	@Override
	public Product save(Product product) {
		 if (repository.findByName(product.getName()).isPresent()){   
	            throw new EntityAlreadyExistException(
	              "There is a product with that name:  "
	              + product.getName());
	        }
		 else {
			 return repository.save(product);
		 }
		 
	}
	
	@Override
	public Product update(Product product) {
		 if (repository.findById(product.getId()).isPresent()){  
			 return repository.save(product);
	           
	        }
		 else {
			 throw new EntityDoesNotExistException(
		              "There is no product to update:  "
		              + product.getName());
		 }
		 
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	

}