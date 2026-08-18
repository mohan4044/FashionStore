package com.fashionstore.dao;

import com.fashionstore.model.Category;
import java.util.List;

public interface CategoryDAO {

    boolean addCategory(Category category);

    Category getCategoryById(int categoryId);

    Category getCategoryByName(String categoryName);

    List<Category> getAllCategories();

    List<Category> getActiveCategories();

    boolean updateCategory(Category category);

    boolean deleteCategory(int categoryId);

    boolean categoryExists(String categoryName);
}