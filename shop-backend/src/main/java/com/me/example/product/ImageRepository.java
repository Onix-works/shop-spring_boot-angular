package com.me.example.product;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

}