package com.shop;

import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }


    @PostConstruct
    public void initializing() {
        Member member = Member.builder()
                .name("123")
                .email("123@123")
                .address("123")
                .password(passwordEncoder.encode("123123123"))
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);
    }


}
