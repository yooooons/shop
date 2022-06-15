package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody @Validated OrderDto orderDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        try {
            orderId = orderService.saveOrder(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);


        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);



        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        List<OrderHistDto> content = orderHistDtoList.getContent();
        for (OrderHistDto orderHistDto : content) {
            List<OrderItemDto> orderItemDtoList = orderHistDto.getOrderItemDtoList();
            for (OrderItemDto orderItemDto : orderItemDtoList) {
                orderItemDto.getItemNm();
            }
        }

        return "order/orderHist";

    }

    @PostMapping("/order/{orderId}/cancel")
    @ResponseBody
    public ResponseEntity cancelOrder(@PathVariable Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
