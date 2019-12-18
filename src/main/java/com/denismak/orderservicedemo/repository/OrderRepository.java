package com.denismak.orderservicedemo.repository;

import com.denismak.orderservicedemo.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
