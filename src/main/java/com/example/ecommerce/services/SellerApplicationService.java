// src/main/java/com/example/ecommerce/services/SellerApplicationService.java
package com.example.ecommerce.services;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.enums.SellerApplicationStatus;
import com.example.ecommerce.models.SellerApplication;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.SellerApplicationRepository;
import com.example.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SellerApplicationService {
    private final SellerApplicationRepository repo;
    private final UserRepository userRepo;

    public SellerApplicationService(SellerApplicationRepository r, UserRepository u) {
        this.repo = r; this.userRepo = u;
    }

    public SellerApplication apply(User user, String note) {
        if (repo.existsByUserIdAndStatus(user.getId(), SellerApplicationStatus.PENDING))
            throw new IllegalStateException("Already applied and waiting for review");
        return repo.save(new SellerApplication(null, user, null,
                SellerApplicationStatus.PENDING, note));
    }

    public List<SellerApplication> listPending() {
        return repo.findByStatus(SellerApplicationStatus.PENDING);
    }

    @Transactional
    public SellerApplication approve(Long id) {
        SellerApplication app = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No application"));
        app.setStatus(SellerApplicationStatus.APPROVED);
        // upgrade role
        User u = app.getUser();
        u.setRole(Role.SELLER);
        userRepo.save(u);
        return app;
    }

    public SellerApplication reject(Long id) {
        SellerApplication app = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No application"));
        app.setStatus(SellerApplicationStatus.REJECTED);
        return repo.save(app);
    }
}
