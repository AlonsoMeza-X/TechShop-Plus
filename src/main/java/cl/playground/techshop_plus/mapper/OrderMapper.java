package cl.playground.techshop_plus.mapper;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderItemsDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderRespondeDto toOrderResponseDto(Order order) {
        return new OrderRespondeDto(
                order.getCustomer().getName(),
                order.getId(),
                order.getOrderDate(),
                order.getOrderItems().stream()
                        .mapToLong(OrderItem::getQuantity)
                        .sum(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }

    public OrderDetailResponseDto toOrderDetailResponseDto(Order order) {
        return new OrderDetailResponseDto(
                order.getCustomer().getName(),
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                toOrderItemsDtoList(order.getOrderItems()),
                order.getOrderItems().stream()
                        .mapToLong(OrderItem::getQuantity)
                        .sum()
        );
    }

    // PRUEBA
//    public OrderDetailResponseDto toOrderDetailResponseDtoBuilder(Order order) {
//        return OrderDetailResponseDto.builder()
//                .customerName(order.getCustomer().getName())
//                .orderId(order.getId())
//                .build();
//    }

    private List<OrderItemsDto> toOrderItemsDtoList(List<OrderItem> items) {
        return items.stream()
                .map(this::toOrderItemsDto)
                .collect(Collectors.toList());
    }

    private OrderItemsDto toOrderItemsDto(OrderItem item) {
        return new OrderItemsDto(
                item.getProductName(),
                item.getPrice(),
                item.getQuantity(),
                item.getPrice() * item.getQuantity()
        );
    }

    // Metodo de utilidad para convertir una lista de órdenes a DTOs
    public List<OrderRespondeDto> toOrderResponseDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponseDto)
                .collect(Collectors.toList());
    }

    // Metodo de utilidad para convertir una lista de órdenes a DTOs detallados
    public List<OrderDetailResponseDto> toOrderDetailResponseDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderDetailResponseDto)
                .collect(Collectors.toList());
    }
}

