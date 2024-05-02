package com.bwd.bwd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bwd.bwd.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM `jobsmith_category_tbl` WHERE archived = 0")
	List<Category> findByCategoryType();	
	
	@Query(nativeQuery = true, value = "SELECT * FROM `jobsmith_category_tbl` WHERE `jobsmith_category_type` = ?1 AND archived = 0")
	List<Category> findByCategoryType(int categoryType);	
	
    List<Category> findAll();
}
