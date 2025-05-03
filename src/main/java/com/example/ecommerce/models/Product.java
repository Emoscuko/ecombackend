package com.example.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    @Column(nullable = false)
    private Integer stock = 0;
    // quantity available

    @JsonIgnoreProperties({"products"})
    @JsonIncludeProperties({"id"})
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @JsonIgnoreProperties({"products"})            // avoid infinite loops
    @JsonIncludeProperties({"id","name"})          // keep payload light
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;





}
