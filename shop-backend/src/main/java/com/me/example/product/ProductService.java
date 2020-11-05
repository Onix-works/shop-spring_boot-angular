package com.me.example.product;

import java.util.List;

public interface ProductService {
	List<Product> findAll();
	Product findById(Long id);
	Product findByName(String name);
	Product save(Product product);
	void delete(Long id);
	Product update(Product product);

}
