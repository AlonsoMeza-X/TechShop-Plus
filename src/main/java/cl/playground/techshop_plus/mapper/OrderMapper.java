package cl.playground.techshop_plus.mapper;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderItemsDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import cl.playground.techshop_plus.model.Order;
import cl.playground.techshop_plus.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public OrderRespondeDto toOrderResponseDtoBuilder(Order order) {
        return OrderRespondeDto.builder() //builder, permite inicar la construccion del Objeto OrderRespondeDto.
                .customerName(order.getCustomer().getName()) // Toma el valor de la objeto order, en este caso  el nombre (getName), y lo agrega o setea el atributo customerName del OrderRespondeDto.
                .orderId(order.getId()) // Toma el valor de la objeto order, en este caso  el id (getId), y lo agrega o setea el atributo orderID del OrderRespondeDto.
                .date(order.getOrderDate()) //Mismo caso para los atributos restantes.
                .totalProducts(order.getOrderItems().stream() //stream la informacion pasa por un filtro
                        .mapToLong(OrderItem::getQuantity)//getQuantity, se sabe cuanto producto pasan.
                        .sum()) /*sum, es como un contador. Aplica a todo lo que esta dentro del stream y todo lo afectado dentro de maptolong.*/
                .totalAmount(order.getTotalAmount()) // =D :3 ;D :0
                .status(order.getStatus()).build();
        //build, permite finalizar y setear los atributos agregados al Objeto OrderRespondeDto.
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
//estoy leyendo la funcion.. y debe terminar en builder? OK

    // pedro alfaro
    public OrderDetailResponseDto toOrderDetailResponseBuilderDto(Order order) {
        return OrderDetailResponseDto.builder() //permite inicar la construccion del Objeto OrderDetailResponseDto
                .customerName(order.getCustomer().getName())//Toma el valor de la objeto order, en este caso  el nombre (getName), y lo agrega o setea el atributo customerName del OrderDetailResponseDto
                .orderId(order.getId())//Asigna el identificador único del pedido (tipo Long) al atributo orderId del DTO.
                .orderDate(order.getOrderDate()) // Establece la fecha del pedido en el atributo orderDate del DTO.
                .totalAmount(order.getTotalAmount())// Asigna el monto total del pedido al atributo totalAmount del DTO.
                .items(toOrderItemsDtoList(order.getOrderItems().stream()
                        .collect(Collectors.toList())))
                /* Convierte la lista de OrderItems en una lista de OrderItemsDto usando
                 * el metodo auxiliar toOrderItemsDtoList El resultado se asigna al
                 * atributo items del DTO. */
                .totalProducts(order.getOrderItems().stream()
                        .mapToLong(OrderItem::getQuantity)
                        .sum())
                /* Utiliza un stream para sumar las cantidades de todos los artículos
                 * del pedido, calculando el total de productos. Este valor se asigna
                 * al atributo totalProducts del DTO. */
                .status(order.getStatus())
                // Establece el estado actual del pedido
                .build();
                // Finaliza la construcción del objeto OrderDetailResponseDto.
    }

    public OrderDetailResponseDto toOrderDetailResponseBuilderDto2(Order order) {
        return OrderDetailResponseDto.builder()
                .customerName(order.getCustomer().getName())
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(toOrderItemsDtoList(order.getOrderItems().stream().toList()))
                .totalProducts(order.getOrderItems().stream()
                        .mapToLong(OrderItem::getQuantity)
                        .sum())
                .build();

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

