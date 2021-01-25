package com.me.example.cart;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.me.example.product.Product;

import lombok.NonNull;

public interface InCartProductRepository extends PagingAndSortingRepository<InCartProduct, Long> {

	@Override
    void delete(InCartProduct inCartProduct);

	Optional<InCartProduct> findByProduct(@NonNull Product product);

}