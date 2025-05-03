package com.example.ecommerce.services;

import com.example.ecommerce.models.Category;
import com.example.ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository categoryRepo;

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }
    public Category updateCategory(Long id, Category details) {
        Category cat = getCategoryById(id);
        cat.setName(details.getName());
        cat.setDescription(details.getDescription());
        return categoryRepo.save(cat);
    }
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }
}
