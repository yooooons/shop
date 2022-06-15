package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.OrderStatus;
import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repo.ItemRepository;
import com.shop.repo.MemberRepository;
import com.shop.repo.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;

    public Item saveItem() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .stockNumber(100)
                .itemSellStatus(ItemSellStatus.SELL)
                .itemDetail("테스트 상세 설명입니다.")
                .build();
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = Member.builder()
                .email("test@test")
                .build();
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    void order() {
        //given
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        //when
        Long orderId = orderService.saveOrder(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDto.getCount() * item.getPrice();

        //then
        assertThat(totalPrice).isEqualTo(order.getTotalPrice());

    }


    @Test
    @DisplayName("주문 취소 테스트")
    void cancelOrder() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.saveOrder(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        assertThat(OrderStatus.CANCEL).isEqualTo(order.getOrderStatus());
        assertThat(100).isEqualTo(item.getStockNumber());
    }


}