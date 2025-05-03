package com.example.ecommerce.services;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.models.*;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.PaymentRepository;
import com.example.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private PaymentRepository paymentRepo;

    @Transactional
    public Order placeOrder(User user, List<OrderItem> items) {
        double total = 0;

        for (OrderItem item : items) {
            Product product = productRepo.findByIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException(
                        product.getName() + " is out of stock (left: " + product.getStock() + ")");
            }

            /* decrement stock & attach price */
            product.setStock(product.getStock() - item.getQuantity());
            productRepo.save(product);

            item.setProduct(product);
            item.setPrice(product.getPrice());
            total += product.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        items.forEach(order::addItem);

        return orderRepo.save(order);
    }
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepo.findByUserEmailWithItems(email);
    }
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepo.save(order);
    }
    // Method to count pending orders for a specific seller
    public int countPendingForSeller(Long sellerId) {
        List<Order> orders = orderRepo.findByUser_Id(sellerId); // Retrieve orders by seller
        int pendingCount = 0;
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PENDING) {
                pendingCount++;
            }
        }
        return pendingCount;
    }

    // Method to calculate the total sales for a specific seller
    public BigDecimal sumPaidTotalForSeller(Long sellerId) {
        BigDecimal totalSales = BigDecimal.ZERO;
        List<Order> orders = orderRepo.findByUser_Id(sellerId); // Retrieve orders by seller

        for (Order order : orders) {
            // Check if payment is completed for the order
            Payment payment = order.getPayment();
            if (payment != null && "succeeded".equals(payment.getStatus())) {
                totalSales = totalSales.add(BigDecimal.valueOf(order.getTotalAmount()));
            }
        }
        return totalSales;
    }
    @Transactional
    public void linkPaymentIntent(Long orderId, String paymentIntentId) {
        Payment payment = paymentRepo.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("No Payment found for order " + orderId));
        payment.setPaymentIntentId(paymentIntentId);
        paymentRepo.save(payment);
    }
}
