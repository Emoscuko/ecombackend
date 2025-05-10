package com.example.ecommerce.controllers.seller;

import com.example.ecommerce.dtos.ProductRequest;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.models.Category;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.models.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/seller/products")
@CrossOrigin(origins = "http://localhost:4200")
public class SellerProductController {
    @Autowired private ProductService productService;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping
 public Product create(@Valid @RequestBody ProductRequest dto, Principal principal) {
             User seller = currentSeller(principal);
             Category cat = categoryRepo.findById(dto.categoryId())
                             .orElseThrow(() ->
                                 new ResponseStatusException(
                                             HttpStatus.BAD_REQUEST,
                                             "Invalid category ID: " + dto.categoryId()
                                                 )
                                     );
             return productService.addProduct(toEntity(dto, cat, seller));
         }
    private User currentSeller(Principal principal) {
        return userRepo.findByEmail(principal.getName()).orElseThrow();
    }
    @GetMapping
    public List<Product> listMine(Principal principal) {
        return productService.getProductsBySeller(currentSeller(principal).getId());
    }
    @GetMapping("/{id}")
    public Product getOne(@PathVariable Long id, Principal principal) {
        Long sellerId = currentSeller(principal).getId();
        Product p = productService.getProductById(id);     // existing method
        if (!p.getSeller().getId().equals(sellerId)) {     // ownership guard
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your product");
        }
        if (!p.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return p;
    }


    private Product toEntity(ProductRequest r, Category c, User s) {
        return new Product(null,true, r.name(), r.description(), r.price(),
                r.imageUrl(), r.stock(), s, c);
    }
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductRequest dto, Principal pr) {
        User seller = currentSeller(pr);
        Category cat = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Bad category"));
        return productService.updateProductForSeller(id, seller.getId(), toEntity(dto, cat, seller));
    }
    /* ---------- delete ---------- */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Principal pr) {
        productService.deleteProductForSeller(id, currentSeller(pr).getId());
    }
}


