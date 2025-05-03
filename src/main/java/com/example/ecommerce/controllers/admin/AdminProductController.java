package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.dtos.ProductRequest;
import com.example.ecommerce.models.Category;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminProductController {
    @Autowired private ProductService productService;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private UserRepository userRepo;
    @PostMapping
    public Product create(@RequestBody ProductRequest req) {
        var category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Bad category"));
        var seller = userRepo.findById(req.sellerId())
                .orElseThrow(() -> new RuntimeException("Bad Seller"));
        return productService.addProduct(toEntity(req, category, seller));
    }
    private Product toEntity(ProductRequest r, Category c, User s) {
        return new Product(null, r.name(), r.description(),
                r.price(), r.imageUrl(), r.stock(), s, c);
    }
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductRequest req) {
        var category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Bad category"));
        var seller = userRepo.findById(req.sellerId())
                .orElseThrow(() -> new RuntimeException("Bad Seller"));
        return productService.updateProduct(id, toEntity(req, category, seller));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

