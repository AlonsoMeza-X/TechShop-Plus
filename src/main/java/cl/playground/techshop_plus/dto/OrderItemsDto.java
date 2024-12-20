package cl.playground.techshop_plus.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemsDto {
    private String productName;
    private Double price;
    private Integer quantity;
    private Double subTotal;
}
