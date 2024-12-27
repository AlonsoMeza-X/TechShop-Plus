package cl.playground.techshop_plus.controller;

import cl.playground.techshop_plus.dto.OrderDetailResponseDto;
import cl.playground.techshop_plus.dto.OrderRespondeDto;
import cl.playground.techshop_plus.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("orders")
public class OrderViewController {

    private final OrderService orderService;

    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public String getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<OrderRespondeDto> orderPage = orderService.findAllOrdersSimplifiedQuery(PageRequest.of(page, size));

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());

        model.addAttribute("size", size);
        model.addAttribute("hasNext", orderPage.hasNext());
        model.addAttribute("hasPrevious", orderPage.hasPrevious());

        return "orderList";
    }

    @GetMapping("/detail/{id}")
    public String getOrderDetails(@PathVariable Long id,Model model) {
        OrderDetailResponseDto orderDetail= orderService.findOrderDetailById(id);
        model.addAttribute("order", orderDetail);
        return "orderDetail";
    }

}
