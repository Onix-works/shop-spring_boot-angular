package com.me.example.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	
	public Optional<Product> findByName(String name);

	public Page<Product> findByCategories(@Param("ctgr") String name, Pageable p); 
	
	@Override
    void delete(Product product);

}