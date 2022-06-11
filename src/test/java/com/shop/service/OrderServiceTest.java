package com.shop.service;

import com.shop.repo.ItemRepository;
import com.shop.repo.MemberRepository;
import com.shop.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository repository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;




}