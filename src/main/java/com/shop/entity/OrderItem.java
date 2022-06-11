package com.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    //연관관계 편의 메서드
    public void changeOrder(Order order) {
        this.order = order;
        order.getOrderItems().add(this);
    }

    public OrderItem() {

    }

    public void changeItemCountOrderPrice(Item item, int count, int orderPrice) {
        this.item = item;
        this.count = count;
        this.orderPrice = orderPrice;
    }

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.changeItemCountOrderPrice(item, count, item.getPrice());
        item.removeStock(count);
        return orderItem;
        //orderItem을 반납하는 과정에서 item의 재고를 줄여줘야 되기에 item.removeStock(count)로직을 사용
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
    

}
