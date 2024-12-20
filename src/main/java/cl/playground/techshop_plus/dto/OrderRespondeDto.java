package cl.playground.techshop_plus.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRespondeDto {
    private String customerName;
    private Long orderId;
    private LocalDateTime date;
    private Long totalProducts;
    private Double totalAmount;
    private String status;

}
