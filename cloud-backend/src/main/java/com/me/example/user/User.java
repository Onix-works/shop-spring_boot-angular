package com.me.example.user;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.me.example.cart.InCartProduct;
import com.me.example.product.Image;

import lombok.Data;
/**
 * Abstract class template for any user without specified credentials 
 * @author Me
 *
 */
@Data
@MappedSuperclass
public class  User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, fetch = FetchType.EAGER)
	protected List<InCartProduct> inCartProducts;
	
	@OneToOne( cascade = CascadeType.ALL)
	protected Image image;
	
	public List<InCartProduct> addInCartProduct(InCartProduct product){
		inCartProducts.add(product);
		return inCartProducts;
	}
	
	/**extracts InCartProducts from UserDTO
	*/
	public void fromDTO(UserDTO user) { 
		setInCartProducts(user.getInCartProducts());
	}
	/**converts to UserDTO 
	*/
	public UserDTO toDTO() { 
		return new UserDTO(this.getId(), "unauthenticated", this.getInCartProducts(), 
				Optional.ofNullable(this.getImage()).orElse(new Image()).getId());
	}
	

}
