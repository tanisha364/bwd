package com.bwd.bwd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "jobsmith_category_tbl")
@Entity
@Getter
@Setter
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "jobsmith_categoryid")	
	private int categoryId;
	
	@Column(name = "jobsmith_category")
	private String category;
	
	@Column(name = "jobsmith_category_type")
	private int categoryType;
	
//	@Column(name = "date_added")
//	private Date dateAdded;
//	
//	@Column(name = "dateModified")
//	private Date dateModified;
//	
//	@Column(name = "archived")
//	private int archived;
}
