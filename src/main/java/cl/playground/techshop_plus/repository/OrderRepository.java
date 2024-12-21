package cl.playground.techshop_plus.repository;

import cl.playground.techshop_plus.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //    List<Order> findAll();
//
//    Optional<Order> findByTotalAmountOrderByCustomerName(Double totalAmount);
//
//    List<Order> findLatestOrdersByCustomer(int i, int i1);

    /*
    * Metodo que retorna todos los atributos de Order, ademas del nombre del customer, email del customer y total de items como numero.
    *
    */
    @Query(value = """
    SELECT 
        o.*, 
        c.name as customer_name,
        c.email as customer_email,
        COUNT(oi.id) as total_items
    FROM orders o
    INNER JOIN customers c ON o.customer_id = c.id
    LEFT JOIN order_items oi ON o.id = oi.order_id
    GROUP BY o.id, c.id
    ORDER BY o.order_date DESC
    LIMIT :#{#pageable.pageSize} 
    OFFSET :#{#pageable.offset}
    """,
            countQuery = """
    SELECT COUNT(DISTINCT o.id) 
    FROM orders o
    """,
            nativeQuery = true)
    Page<Order> findAllOrdersWithDetails(Pageable pageable);


    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
}
