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

/*
* INFORMACION PRINCIPAL QUE SERA MOSTRADA EN LA VISTA
* CONTENDRA:
*  - NOMBRE DEL CLIENTE
*  - IDENTIFICADOR DE LA ORDEN
*  - FECHA DE LA ORDEN
*  - CANTIDAD DE PRODUCTOS EN LA ORDEN (TOTAL)
*  - VALOR TOTAL DE LA ORDEN A PAGAR
*  - ESTADO DE LA ORDEN
*/
