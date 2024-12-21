package cl.playground.techshop_plus.service;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderRespondeDto> findAllOrdersSimplifiedQuery(Pageable pageable);
    Page<OrderDetailResponseDto> findAllOrdersWithDetailsQuery(Pageable pageable);


    Page<OrderRespondeDto> findAllOrdersSimplifiedHibernate(Pageable pageable);
    Page<OrderDetailResponseDto> findAllOrdersWithDetailsHibernate(Pageable pageable);

}
