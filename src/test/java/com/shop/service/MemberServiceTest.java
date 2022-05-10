package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void saveMemberTest() {
        //given
        Member member = createMember();
        //when
        Member saveMember = memberService.saveMember(member);
        //then
        assertThat(member.getEmail()).isEqualTo(saveMember.getEmail());
        assertThat(member.getAddress()).isEqualTo(saveMember.getAddress());
        assertThat(member.getName()).isEqualTo(saveMember.getName());
        assertThat(member.getPassword()).isEqualTo(saveMember.getPassword());
        assertThat(member.getRole()).isEqualTo(saveMember.getRole());
    }
    @Test
    @DisplayName("중복 회원 가입 테스트")
    void saveDuplicateMemberTest() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        //when
        //then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> memberService.saveMember(member2));
        assertThat(illegalStateException.getMessage()).isEqualTo("이미 가입된 회원입니다.");
    }
}