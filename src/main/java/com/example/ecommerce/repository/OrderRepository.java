package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.status = :status")
//    List<Order> findByCustomerIdAndStatus(Long customerId, String status);
}
