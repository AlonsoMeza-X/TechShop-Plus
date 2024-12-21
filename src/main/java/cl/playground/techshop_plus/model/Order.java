package cl.playground.techshop_plus.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "status", length = 20)
    private String status;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();

        if (status == null) {
            this.status = "pending";
        }

        if (totalAmount == null) {
            this.totalAmount = 0.0;
        }
    }

}
