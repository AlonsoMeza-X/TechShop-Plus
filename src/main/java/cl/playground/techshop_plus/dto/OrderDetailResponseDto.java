package cl.playground.techshop_plus.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetailResponseDto {
    private String customerName;
    private Long orderId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private List<OrderItemsDto> items;
    private Long totalProducts;
}

