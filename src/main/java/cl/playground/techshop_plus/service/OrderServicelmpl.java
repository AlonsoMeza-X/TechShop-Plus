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
public class OrderServicelmpl implements OrderService  {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    public OrderServicelmpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Page<OrderRespondeDto> findAllOrdersSimplifiedQuery(Pageable pageable) {
        return orderRepository.findAllOrdersWithDetails(pageable)
                .map(orderMapper::toOrderResponseDto);
    }

    @Override
    public Page<OrderDetailResponseDto> findAllOrdersWithDetailsQuery(Pageable pageable) {
        return orderRepository.findAllOrdersWithDetails(pageable)
                .map(orderMapper::toOrderDetailResponseDto);
    }

    @Override
    public Page<OrderRespondeDto> findAllOrdersSimplifiedHibernate(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        return ordersPage.map(orderMapper::toOrderResponseDto);
    }

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
}
