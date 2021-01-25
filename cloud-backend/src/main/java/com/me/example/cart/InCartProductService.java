package com.me.example.cart;

import java.util.List;

public interface InCartProductService {
	List<InCartProduct> findAll();
	InCartProduct findById(Long id);
	InCartProduct save(InCartProduct inCartProduct);
	void delete(Long id);
	InCartProduct update(InCartProduct inCartProduct);

}