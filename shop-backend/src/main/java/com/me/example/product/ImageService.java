package com.me.example.product;

import java.util.List;

public interface ImageService {
	List<Image> findAll();
	Image findById(Long id);
	Image save(Image image);
	void delete(Long id);
	Image update(Image image);

}