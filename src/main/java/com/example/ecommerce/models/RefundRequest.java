package com.example.ecommerce.models;

import com.example.ecommerce.enums.RefundStatus;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)          // don’t output nulls
public class RefundRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- relations ---------- */

    @JsonIgnore                                         // prevent proxy serialisation
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;

    /* ---------- scalar fields ---------- */

    @Column(nullable = false, length = 800)
    private String reason;

    @Enumerated(EnumType.STRING)
    private RefundStatus status = RefundStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    /** admin decision data */
    private LocalDateTime decidedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User decidedBy;        // admin user – we don’t expose full object

    private String adminComment;

    /* ---------- virtual properties sent to the client ---------- */

    @JsonProperty("userId")
    public Long getUserId() {      // exposes scalar instead of proxy
        return user != null ? user.getId() : null;
    }

    @JsonProperty("orderId")
    public Long getOrderId() {
        return order != null ? order.getId() : null;
    }
}
