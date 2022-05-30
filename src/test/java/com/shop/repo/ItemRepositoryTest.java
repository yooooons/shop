package com.shop.repo;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    void createItemTest() {
        Item item = Item.builder()
                .itemNm("테스트상품")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
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
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }
    @Test
    @DisplayName("가격 LessThan 테스트")
    void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> byPriceLessThan = itemRepository.findByPriceLessThan(10005);
        for (Item item : byPriceLessThan) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("가격 내림차수 조회테스트")
    void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> byPriceLessThanOrderByPriceDesc = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : byPriceLessThanOrderByPriceDesc) {
            System.out.println("item = " + item);
        }

    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("Querydsl 조회테스트1")
    void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" +
                        "테스트 상품 상세 설명" +
                        "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    void queryDslTest2() {
        this.createItemList2();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat,ItemSellStatus.SELL.name())) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("itemPagingResult.getTotalElements() = " + itemPagingResult.getTotalElements());

        List<Item> resultContent = itemPagingResult.getContent();
        for (Item resultItem : resultContent) {
            System.out.println("resultItem = " + resultItem);
        }
    }


    private void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품"+i)
                    .price(10000+i)
                    .itemDetail("테스트 상품 상세 설명"+i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build();
            Item saveItem = itemRepository.save(item);
        }
    }

    private void createItemList2() {
        for (int i = 0; i <= 5; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100)
                    .build();
            Item saveItem = itemRepository.save(item);
        }
        for (int i = 6; i <= 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .stockNumber(0)
                    .build();
            Item saveItem = itemRepository.save(item);
        }
    }

}