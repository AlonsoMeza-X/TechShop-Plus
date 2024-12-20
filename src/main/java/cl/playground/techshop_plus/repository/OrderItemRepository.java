package cl.playground.techshop_plus.repository;


import cl.playground.techshop_plus.model.Customer;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
