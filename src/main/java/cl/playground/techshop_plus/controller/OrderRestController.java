package cl.playground.techshop_plus.controller;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import cl.playground.techshop_plus.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/simplified")
    public ResponseEntity<Map<String, Object>> getOrdersSimplified(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<OrderRespondeDto> orderPage = orderService.findAllOrdersSimplifiedQuery(
                PageRequest.of(page, size)
        );

        return ResponseEntity.ok(createPaginationResponse(orderPage));
    }

    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> getOrdersDetailed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<OrderDetailResponseDto> orderPage = orderService.findAllOrdersWithDetailsQuery(
                PageRequest.of(page, size)
        );

        return ResponseEntity.ok(createPaginationResponse(orderPage));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(@PathVariable Long id) {
        OrderDetailResponseDto orderDetailResponseDto = orderService.findOrderDetailById(id);

        if (orderDetailResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailResponseDto);
    }

    private Map<String, Object> createPaginationResponse(Page<?> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("size", page.getSize());
        response.put("hasNext", page.hasNext());
        response.put("hasPrevious", page.hasPrevious());
        return response;
    }
}
