package com.shop.entity;

import com.shop.repo.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "yoon", roles = "USER")
    void auditingTest() {
        Member newMember = Member.builder()
                .build();
        memberRepository.save(newMember);
        entityManager.flush();
        entityManager.clear();
        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("member.getRegTime() = " + member.getRegTime());
        System.out.println("member.getUpdateTime() = " + member.getUpdateTime());
        System.out.println("member.getCreateBy() = " + member.getCreateBy());
        System.out.println("member.getModifiedBy() = " + member.getModifiedBy());
    }
}