package com.bwd.bwd.response;

import java.util.List;

import com.bwd.bwd.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
	public List<Category> categories;
}
