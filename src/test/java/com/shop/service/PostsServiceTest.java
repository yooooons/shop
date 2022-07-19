package com.shop.service;

import com.shop.constant.Role;
import com.shop.dto.PostsDto;
import com.shop.entity.Member;
import com.shop.entity.Posts;
import com.shop.repo.MemberRepository;
import com.shop.repo.PostsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @InjectMocks
    PostsService postsService;


    @Mock
    MemberRepository memberRepository;
    @Mock
    PostsRepository postsRepository;


    @Test
    @DisplayName("포스트 저장")
    void savePost() {
        //given
        Optional<Member> member = Optional.of(Member.builder()
                .id(2L)
                .email("123@123")
                .address("123")
                .name("123")
                .password("123")
                .role(Role.USER)
                .build());
        Mockito.when(memberRepository.findByEmail("123@123")).thenReturn(member);

        PostsDto.Request request = PostsDto.Request
                .builder()
                .id(1L)
                .writer("123")
                .contents("123123")
                .title("123123")
                .build();

        request.setMember(member.orElseThrow(EntityNotFoundException::new));
        Mockito.when(postsRepository.save(any(Posts.class))).thenReturn(request.toEntity());
        Long postId = postsService.savePost(request, "123@123");
        assertThat(1L).isEqualTo(postId);


    }


}