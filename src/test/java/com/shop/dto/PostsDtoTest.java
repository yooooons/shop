package com.shop.dto;

import com.shop.entity.Member;
import com.shop.entity.Posts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class PostsDtoTest {


    @Test
    void toEntity() {
        //given
        PostsDto.Request request = PostsDto.Request
                .builder()
                .writer("테스트사용자")
                .contents("테스트 내용")
                .title("테스트 제목")
                .build();
        Member member = Member
                .builder()
                .id(1L)
                .build();


        //when
        request.setMember(member);
        Posts posts = request.toEntity();

        //then
        Assertions.assertThat(posts.getWriter()).isEqualTo("테스트사용자");
        Assertions.assertThat(posts.getContents()).isEqualTo("테스트 내용");
        Assertions.assertThat(posts.getTitle()).isEqualTo("테스트 제목");
        Assertions.assertThat(posts.getMember().getId()).isEqualTo(1L);

    }

}