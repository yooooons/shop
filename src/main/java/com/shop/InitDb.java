package com.shop;

import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitDb {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            Member member = Member.builder()
                    .name("test")
                    .email("test@test")
                    .address("test")
                    .password(passwordEncoder.encode("123123123"))
                    .role(Role.ADMIN)
                    .build();
            memberRepository.save(member);
        }
    }
}
