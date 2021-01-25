package com.me.example.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.me.example.exceptions.EntityAlreadyExistException;
import com.me.example.exceptions.EntityDoesNotExistException;

@Service("inCartProductService")
public class InCartProductServiceImp implements InCartProductService {
	
	@Autowired
	InCartProductRepository repository;


	@Override
	public List<InCartProduct> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public InCartProduct findById(Long id) {
		if (repository.findById(id).isPresent()){   
			return repository.findById(id).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No InCartProduct with such id.");
		 }
	}


	@Override
	public InCartProduct save(InCartProduct inCartProduct) {
		 if (repository.findByProduct(inCartProduct.getProduct()).isPresent()){   
	            throw new EntityAlreadyExistException(
	              "There  is InCartProduct with that product :  "
	              + inCartProduct.getId());
	        }
		 else {
			 return repository.save(inCartProduct);
		 }
		 
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	@Override
	public InCartProduct update(InCartProduct inCartProduct) {
		 if (repository.findById(inCartProduct.getId()).isPresent()){   
			 return repository.save(inCartProduct);
	        }
		 else {
			 throw new EntityDoesNotExistException(
		              "There  is no InCartProduct to update :  "
		              + inCartProduct.getId());
		 }
		 
	}
	
	

}
