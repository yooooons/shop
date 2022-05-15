package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Member {


    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {

    }


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(password)
                .role(Role.USER)
                .build();
        return member;
    }
}
