package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductRepository productRepo;



    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public Product addProduct(Product product) {
        // Assume product.category is set to an existing category
        return productRepo.save(product);
    }
    /* ---------- seller CRUD guarded by ownership ---------- */


    public Product updateProductForSeller(Long id, Long sellerId, Product data) {
        Product p = mustBeOwner(id, sellerId);
        copy(p, data);
        return productRepo.save(p);
    }

    public void deleteProductForSeller(Long id, Long sellerId) {
        Product p = mustBeOwner(id, sellerId);
        p.setActive(false);
        productRepo.save(p);
    }

    private Product mustBeOwner(Long prodId, Long sellerId) {
        Product p = getProductById(prodId);
        if (!p.getSeller().getId().equals(sellerId))
            throw new SecurityException("Not your product");
        return p;
    }
    public List<Product> getAllAdminProducts() {
                return productRepo.findAll();
            }
    /** Admin-only: reactivate */
    public Product activateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(true);
        return productRepo.save(p);
    }
    private void copy(Product p, Product d) {          // tiny mapper
        p.setName(d.getName());  p.setDescription(d.getDescription());
        p.setPrice(d.getPrice()); p.setStock(d.getStock());
        p.setImageUrl(d.getImageUrl()); p.setCategory(d.getCategory());
    }
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        // Update fields
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());
        return productRepo.save(product);
    }
    public void deleteProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        productRepo.save(p);
    }
    public List<Product> getAllProducts() {
        return productRepo.findAllByActiveTrue();
    }
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepo.findByCategory_IdAndActiveTrue(categoryId);
    }
    public List<Product> getProductsBySeller(Long sid) {
        return productRepo.findBySeller_IdAndActiveTrue(sid);
    }

}


