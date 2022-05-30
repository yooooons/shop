package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repo.ItemRepository;
import com.shop.repo.MemberRepository;
import com.shop.repo.OrderItemRepository;
import com.shop.repo.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @PersistenceContext
    EntityManager entityManager;

    public Item createItem() {
        Item item = Item.builder()
                .itemNm("테스트용 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();
        return item;
    }

    public Order createOrder() {
        Order order = Order.builder()
                .build();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);

        }

        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        order.changeMember(member);
        orderRepository.save(order);
        return order;

    }




    @Test
    @DisplayName("영속성 전이 테스트")
    void cascadeTest() {

        Order order = Order.builder()
                .build();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        System.out.println("order.getId() = " + order.getId());
        entityManager.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        Assertions.assertThat(3).isEqualTo(savedOrder.getOrderItems().size());

    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        entityManager.flush();
    }


    @Test
    @DisplayName("지연 로딩 테스트")
    void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        entityManager.flush();
        entityManager.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        System.out.println("orderItem.getOrder().getClass() = " + orderItem.getOrder().getClass());
        System.out.println("=============================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=============================");
    }

}