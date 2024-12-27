package cl.playground.techshop_plus.service;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import cl.playground.techshop_plus.mapper.OrderMapper;
import cl.playground.techshop_plus.model.Customer;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import cl.playground.techshop_plus.repository.CustomerRepository;
import cl.playground.techshop_plus.repository.OrderItemRepository;
import cl.playground.techshop_plus.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServicelmpl implements OrderService {

    /*
     * Variables necesarias para poder hacer inyeccion de dependencias
     * De esta forma podemos aprovechar los beneficios creados en esas clases
     * Dentro de la que estamos construyendo actualmente
     */
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;


    /*
     * Como las variables asignadas arriba son de tipo 'final'
     * El constructor esta obligado a usarlas todas
     */
    public OrderServicelmpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    /*
     * Funcion que trabaja con paginacion
     * Recibe un objeto de paginacion como solicitud
     * Retorna un conjunto de datos 'OrderResponseDTo'
     * Entregados de acuerdo a las cantidades especificadas en la paginacion
     * Este metodo utiliza una Query SQL Nativa para funcionar.
     */
    @Override
    public Page<OrderRespondeDto> findAllOrdersSimplifiedQuery(Pageable pageable) {
        return orderRepository.findAllOrdersWithDetails(pageable)
                .map(orderMapper::toOrderResponseDto);
    }

    /*
     * Funcion que trabaja con paginacion
     * Recibe un objeto de paginacion como solicitud
     * Retorna un conjunto de datos 'OrderResponseDTo'
     * Entregados de acuerdo a las cantidades especificadas en la paginacion
     * Este metodo utiliza una Query SQL Nativa para funcionar.
     *
     * Este metodo aprovecha todos los detalles obtenidos en la query
     * De esa forma genera un DTO mas robusto
     */
    @Override
    public Page<OrderDetailResponseDto> findAllOrdersWithDetailsQuery(Pageable pageable) {
        return orderRepository.findAllOrdersWithDetails(pageable)
                .map(orderMapper::toOrderDetailResponseDto);
    }

    /*
     * Recibe los datos a traves de un metodo propio de JPA/Hibernate
     * De esta forma obtiene los datos aprovechando el mapeo de Hibernate de las entidades
     * Recibe un conjunto de datos y los almacena en un DTO no especifico para su uso
     */
    @Override
    public Page<OrderRespondeDto> findAllOrdersSimplifiedHibernate(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        return ordersPage.map(orderMapper::toOrderResponseDto);
    }

    /*
     * Recibe los datos a traves de un metodo propio de JPA/Hibernate
     * De esta forma obtiene los datos aprovechando el mapeo de Hibernate de las entidades
     * Recibe un conjunto de datos y los almacena en un DTO muy especifico para su uso
     *
     * Este metodo se usa en conbinacion con los demas repositorios necesarias para obtener todos los datos
     * Referente a la Orden y sus demas clases relacionadas
     */
    @Override
    public Page<OrderDetailResponseDto> findAllOrdersWithDetailsHibernate(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        List<Order> orders = orderPage.getContent();

        orders.forEach(order -> {
            Customer customer = customerRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado ID: " + order.getCustomer().getId()));
            order.setCustomer(customer);
        });

        orders.forEach(order -> {
            List<OrderItem> items = orderItemRepository.findByOrder_Id(order.getId());
            order.setOrderItems(items);
        });

        List<OrderDetailResponseDto> orderDtos = orderMapper.toOrderDetailResponseDtoList(orders);

        return new PageImpl<>(
                orderDtos,
                pageable,
                orderPage.getTotalElements()
        );
    }

    @Override
    public OrderDetailResponseDto findOrderDetailById(Long id) {
        return orderMapper.toOrderDetailResponseDto(orderRepository.findById(id).orElse(null));
    }
}
