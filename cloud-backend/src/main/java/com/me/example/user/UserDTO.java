package com.me.example.user;

import java.util.ArrayList;
import java.util.List;

import com.me.example.cart.InCartProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserDTO {
	
	@NonNull
    private Long id;
	@NonNull
	private String name;
	private List<InCartProduct> inCartProducts;
	private Long imageId;

}

