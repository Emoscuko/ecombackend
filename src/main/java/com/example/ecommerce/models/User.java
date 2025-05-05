package com.example.ecommerce.models;

import com.example.ecommerce.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;      // used as username for authentication

    private String name;

    @Column(nullable = false)
    private String password;   // hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;         // e.g. CUSTOMER, ADMIN or SELLER

    private boolean enabled = true;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    @JsonIgnore
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private List<Address> addresses;


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    // Optionally, relationships like orders or reviews
    // @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    // private List<Order> orders;
}
