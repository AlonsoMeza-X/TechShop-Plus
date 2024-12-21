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

/*
* INFORMACION DETALLADA DE CADA ITEM QUE SERA PRESENTADO EN EL DETALLE DE ORDEN
* CONTENDRA:
*  - NOMBRE DEL PRODUCTO
*  - PRECIO UNITARIO DEL PRODUCTO
*  - CANTIDAD DE PRODUCTO INDIVIDUAL EN LA ORDEN
*  - SUB TOTAL DE CADA PRODUCTO (PRECIO X CANTIDAD)
*/