package com.quickcrave.repository;

import com.quickcrave.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemReopsitory extends JpaRepository<OrderItem,Long> {
}
