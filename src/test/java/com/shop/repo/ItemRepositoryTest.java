package com.shop.repo;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void createItemTest() {
        Item item = Item.builder()
                .itemNm("테스트상품")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        Item saveItem = itemRepository.save(item);
        System.out.println("saveItem = " + saveItem);

    }


    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    void findByItemNmOrDerailTest() {

    }

    private void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트 상품 상세 설명"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            Item saveItem = itemRepository.save(item);
        }
    }

}