package cl.playground.techshop_plus.model;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;
    @Column(name="product_name",nullable = false, length = 100)
    private String productName;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "quantity", nullable = false)
    private int quantity;

}
