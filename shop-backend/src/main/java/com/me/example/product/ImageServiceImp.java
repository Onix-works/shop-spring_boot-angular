package com.me.example.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.me.example.exceptions.EntityAlreadyExistException;
import com.me.example.exceptions.EntityDoesNotExistException;

@Service("imageService")
public class ImageServiceImp implements ImageService {
	
	@Autowired
	ImageRepository repository;


	@Override
	public List<Image> findAll() {
		return Lists.newArrayList(repository.findAll());
	}

	@Override
	public Image findById(Long id) {
		if (repository.findById(id).isPresent()){   
			return repository.findById(id).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No product with such id.");
		 }
	}


	@Override
	public Image save(Image image) {
		 if (image.getId() != null && repository.findById(image.getId()).isPresent()){   
	            throw new EntityAlreadyExistException(
	              "There is a Image with that id:  "
	              + image.getId());
	        }
		 else {
			 return repository.save(image);
		 }
		 
	}
	
	@Override
	public Image update(Image image) {
		 if (repository.findById(image.getId()).isPresent()){  
			 return repository.save(image);
	           
	        }
		 else {
			 throw new EntityDoesNotExistException(
		              "There is no image to update:  "
		              + image.getId());
		 }
		 
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	

}