package cl.playground.techshop_plus.repository;

import cl.playground.techshop_plus.model.Customer;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Primero las eliminaciones
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();

        // Luego resetear los auto-incrementos para MySQL
        entityManager.createNativeQuery("ALTER TABLE order_items AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE orders AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE customers AUTO_INCREMENT = 1").executeUpdate();

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

         /*SE CREA LA TERCERA ITEM A TRAVES DE LA TERCERA ORDEN
         *SE REGISTRA EL TERCER ITEM
         *
         * - Funcion FLush()
         *  -> garantiza que los datos  esten almacenados temporalmente en un buffer
         *  -> (memoria intermedia) se envíen a su destino final.
         *
         * - funcion persist()
         *  -> generalmente está relacionado con la persistencia de datos, es decir, guardar información de manera permanente
         *  -> en un almacenamiento, como una base de datos o un archivo, para que pueda ser recuperada posteriormente.
         *
         *  -Diferencia entre persist() y merge() en JPA
               -> persist(): Inserta una nueva entidad en el contexto de persistencia y la marca como gestionada.
               -> merge(): Actualiza una entidad existente o la sincroniza con el contexto de persistencia.
         *
         * */

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
        System.out.println("orders" + orders.size());

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

    @Test
    void findAllOrdersWithCustomerAndItems() {

        // AGREGAMOS PAGINACION
        Pageable pageable = PageRequest.of(0, 10);

        /*
        * PRIMERO OBTENGO LOS ELEMENTOS PAGINADOS
        * DESPUES LOS ALMACENO EN UNA ESTRUCTURA DE DATOS COMO LO ES LA LISTA
        */
        Page<Order> ordersPage = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        List<Order> orders = ordersPage.getContent();
        System.out.println(orders);

        /*
        * ORDERS CONTIENE UNA LISTA DE ELEMENTOS 'Order'
        * USAMOS UN FOREACH PARA INTERACTUAR CON CADA ELEMENTO EN ESA LISTA
        * CADA ORDER SOLO TIENE LA INFORMACION DE LA CLAVE FORANEA DE LOS CUSTOMER
        * USAMOS EL ID DE LA FORANEA PARA APROVECHAR EL REPOSITORIO DEL CUSTOMER PARA RECUPERAR TODOS SUS REGISTROS
        * FINALMENTE ASIGNAMOS EL CUSTOMER RECUPERADO AL ATRIBUTO CORRESPONDIENTE DE LA CLASE ORDER
        */
        for (Order order : orders) {
            Customer customer = customerRepository.findById(order.getCustomer().getId()).orElse(null);
            order.setCustomer(customer);
        }
        System.out.println(orders);

        /*
        * USAMOS OTRO TIPO DE FOR, DETENIENDOLO UN ELEMENTO ANTES DEL SIZE PARA NO EXCEDER EL LIMITE
        * POR CADA Order EN EL LISTADO LE RECUPERAMOS UN LISTADO DE Items QUE APUNTAN AL ID DE LA Order
        * PARA POSTERIORMENTE ASIGNARLO A LA ORDER LA CUAL TIENE UN LIST<OrderItems> ESPERANDOLO.
        */
        for (int i = 0 ; i < orders.size(); i++) {
            Order order = orders.get(i);
            List<OrderItem> items = orderItemRepository.findByOrder_Id(order.getId());
            order.setOrderItems(items);
        }
        System.out.println(orders);

        Order firstorder = orders.get(0);
        assertEquals(3, firstorder.getId());
        assertEquals("María García", firstorder.getCustomer().getName());
        assertEquals("maria@email.com", firstorder.getCustomer().getEmail());
        assertEquals(1, firstorder.getOrderItems().size());

        Order secondOrder = orders.get(1);
        assertEquals(2, secondOrder.getId());
        assertEquals("Juan Pérez", secondOrder.getCustomer().getName());
        assertEquals("juan@email.com", secondOrder.getCustomer().getEmail());
        assertEquals(1, secondOrder.getOrderItems().size());

        Order thirdOrder = orders.get(2);
        assertEquals(1, thirdOrder.getId());
        assertEquals("Juan Pérez", thirdOrder.getCustomer().getName());
        assertEquals("juan@email.com", thirdOrder.getCustomer().getEmail());
    }

}