package com.me.example.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Product  {
	
	public Product(String name, double price, int inStock){
		this.name = name;
		this.price = price;
		this.inStock = inStock;
		this.qualities = new HashMap<String, String>();
		this.categories = new ArrayList<String>();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private double price;
	
	private int inStock;
	
	@Column(length = 4000)
	private String description;
	
	@ElementCollection
	Map<String, String> qualities;
	
	@ElementCollection
	List<String> categories;
	
	@OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})
	Image image;

}