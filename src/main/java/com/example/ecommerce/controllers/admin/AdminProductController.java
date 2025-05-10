// src/main/java/com/example/ecommerce/controllers/admin/AdminProductController.java

package com.example.ecommerce.controllers.admin;

import com.example.ecommerce.dtos.ProductRequest;
import com.example.ecommerce.models.Category;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private UserRepository userRepo;

    /** Admin: list all products (active & inactive) */
    @GetMapping
    public List<Product> listAll() {
        return productService.getAllAdminProducts();
    }

    /** Reactivate */
    @PutMapping("/{id}/activate")
    public Product activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id);
    }

    /** Admin: fetch a single product by ID */
    @GetMapping("/{id}")
    public Product getOne(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /** Admin: create a new product */
    @PostMapping
 public Product create(@Valid @RequestBody ProductRequest req) {
             // IDs are @NotNull, but let’s still translate lookup-failures into 400s:
                     Category category = categoryRepo.findById(req.categoryId())
                             .orElseThrow(() ->
                                 new ResponseStatusException(
                                             HttpStatus.BAD_REQUEST,
                                            "Invalid category ID: " + req.categoryId()
                                                )
                                   );User seller = userRepo.findById(req.sellerId())
                            .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid seller ID: " + req.sellerId()
                                                 )
                                     );
             return productService.addProduct(toEntity(req, category, seller));
         }

    /** Admin: update an existing product */
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id,
                          @RequestBody ProductRequest req) {

        Product current = productService.getProductById(id);
        Category category = categoryRepo.findById(req.categoryId())
                         .orElseThrow(() ->
                             new ResponseStatusException(
                                         HttpStatus.BAD_REQUEST,
                                         "Invalid category ID: " + req.categoryId()
                                             )
                                 );

        // keep the same seller on update
        return productService.updateProduct(
                id,
                new Product(
                        null,
                        true,
                        req.name(),
                        req.description(),
                        req.price(),
                        req.imageUrl(),
                        req.stock(),
                        current.getSeller(),
                        category
                )
        );
    }

    /** Admin: soft-delete a product */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // helper to convert DTO → entity
    private Product toEntity(ProductRequest r, Category c, User s) {
        return new Product(
                null,
                true,
                r.name(),
                r.description(),
                r.price(),
                r.imageUrl(),
                r.stock(),
                s,
                c
        );
    }
}
