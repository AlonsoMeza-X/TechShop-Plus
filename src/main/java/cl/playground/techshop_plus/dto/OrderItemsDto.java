package cl.playground.techshop_plus.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemsDto {
    private String productName;
    private Double price;
    private Integer quantity;
    private Double subTotal;


   // public OrderItemsDto() {}



}

// @RequiredArgsConstructor, te obliga a tener los argumentos que tiene "final" como parte del atributo.

/*
* INFORMACION DETALLADA DE CADA ITEM QUE SERA PRESENTADO EN EL DETALLE DE ORDEN
* CONTENDRA:
*  - NOMBRE DEL PRODUCTO
*  - PRECIO UNITARIO DEL PRODUCTO
*  - CANTIDAD DE PRODUCTO INDIVIDUAL EN LA ORDEN
*  - SUB TOTAL DE CADA PRODUCTO (PRECIO X CANTIDAD)
*/