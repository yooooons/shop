package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;



    public Item() {
    }
}
