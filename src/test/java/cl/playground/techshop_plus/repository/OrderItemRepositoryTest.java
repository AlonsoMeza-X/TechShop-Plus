package cl.playground.techshop_plus.repository;

import cl.playground.techshop_plus.model.Customer;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Crear cliente 1 con dos órdenes
        Customer customer1 = new Customer();
        customer1.setName("Juan Pérez");
        customer1.setEmail("juan@email.com");
        entityManager.persist(customer1);

        // Orden más antigua del cliente 1
        Order order1 = new Order();
        order1.setCustomer(customer1);
//        order1.setOrderDate(now.minusDays(2));  // 2 días antes
        order1.setStatus("completed");
        order1.setTotalAmount(100.0);
        entityManager.persist(order1);

        OrderItem item1 = new OrderItem();
        item1.setOrder(order1);
        item1.setProductName("Producto 1");
        item1.setPrice(50.0);
        item1.setQuantity(2);
        entityManager.persist(item1);

        // Orden más reciente del cliente 1
        Order order2 = new Order();
        order2.setCustomer(customer1);
//        order2.setOrderDate(now);  // Tiempo actual - será la más reciente
        order2.setStatus("pending");
        order2.setTotalAmount(200.0);
        entityManager.persist(order2);

        OrderItem item2 = new OrderItem();
        item2.setOrder(order2);
        item2.setProductName("Producto 2");
        item2.setPrice(100.0);
        item2.setQuantity(2);
        entityManager.persist(item2);

        // Crear cliente 2 con una orden
        Customer customer2 = new Customer();
        customer2.setName("María García");
        customer2.setEmail("maria@email.com");
        entityManager.persist(customer2);

        Order order3 = new Order();
        order3.setCustomer(customer2);
//        order3.setOrderDate(now.minusHours(12));  // 12 horas antes
        order3.setStatus("completed");
        order3.setTotalAmount(150.0);
        entityManager.persist(order3);

        OrderItem item3 = new OrderItem();
        item3.setOrder(order3);
        item3.setProductName("Producto 3");
        item3.setPrice(75.0);
        item3.setQuantity(2);
        entityManager.persist(item3);

        entityManager.flush();
        entityManager.clear(); // Limpia el contexto de persistencia
    }

    @Test
    void findAllOrdersWithDetails_ShouldReturnOrderedOrders() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Order> ordersPage = orderRepository.findAllOrdersWithDetails(pageable);

        // Then
        assertNotNull(ordersPage, "La página de órdenes no debería ser null");
        assertEquals(3, ordersPage.getTotalElements(), "Deberían haber 3 órdenes en total");


        List<Order> orders = ordersPage.getContent();

        System.out.println(orders);

        Order firstOrder = orders.get(0);

        // Verificar la primera orden (la más reciente)
        assertEquals("María García", firstOrder.getCustomer().getName());
        assertEquals(3, firstOrder.getId());
        assertNotNull(firstOrder.getOrderDate());
        assertEquals(1, firstOrder.getOrderItems().size());
        assertEquals(150.0, firstOrder.getTotalAmount());

        // Verificar ordenamiento por fecha
        LocalDateTime previousDate = null;
        for (Order order : orders) {
            if (previousDate != null) {
                assertTrue(previousDate.isAfter(order.getOrderDate()) ||
                                previousDate.isEqual(order.getOrderDate()),
                        "Las órdenes deberían estar ordenadas por fecha descendente");
            }
            previousDate = order.getOrderDate();
        }
    }

//    @Test
//    void findLatestOrdersByCustomer_ShouldRespectPagination() {
//        // Given
//        List<Order> firstPage = orderRepository.findLatestOrdersByCustomer(1, 0);
//
//        // Then
//        assertEquals(1, firstPage.size(),
//                "La primera página debería contener solo una orden");
//        assertEquals("Juan Pérez", firstPage.get(0).getCustomer().getName(),
//                "La primera orden debería ser de Juan Pérez");
//
//        // When
//        List<Order> secondPage = orderRepository.findLatestOrdersByCustomer(1, 1);
//
//        // Then
//        assertEquals(1, secondPage.size(),
//                "La segunda página debería contener solo una orden");
//        assertEquals("María García", secondPage.get(0).getCustomer().getName(),
//                "La segunda orden debería ser de María García");
//    }
}