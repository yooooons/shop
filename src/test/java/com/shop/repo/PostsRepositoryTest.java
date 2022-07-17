package com.shop.repo;

import com.shop.entity.Posts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @BeforeEach
    public void postSave() {
        for (int i = 0; i < 10; i++) {

            Posts post = Posts.builder()
                    .title("테스트 "+i)
                    .contents("테스트 내용"+i)
                    .email("123@"+i)
                    .writer(String.valueOf(i))
                    .view(0)
                    .build();
            postsRepository.save(post);
        }



    }

    @Test
    @DisplayName("게시글 저장")
    void save() {
        //given
        Posts post = Posts.builder()
                .title("테스트 1")
                .contents("테스트 내용")
                .email("123@123")
                .writer("123")
                .view(0)
                .build();
        //when
        Posts savePost = postsRepository.save(post);
        //then
        assertThat(post.getComments()).isEqualTo(savePost.getComments());
        assertThat(savePost.getContents()).isEqualTo("테스트 내용");
    }


    @Test
    @DisplayName("조회수 올리기 테스트")
    void updateView() {
        //given
        List<Posts> posts = postsRepository.findAll();
        Posts post = posts.get(0);
        //when
        int i = postsRepository.updateView(post.getId());
        Posts post2 = postsRepository.getById(post.getId());
        System.out.println("post.getView() = " + post.getView());
        System.out.println("post2.getView() = " + post2.getView());
        System.out.println("i = " + i);
        //then
        assertThat(post.getView()+1).isEqualTo(post2.getView());




    }

    @Test
    @DisplayName("게시글 찾기")
    void findByTitleContaining() {
        //given
//        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<Posts> postsPage = postsRepository.findByTitleContaining("테스트", pageable);
        System.out.println("postsPage.getTotalElements() = " + postsPage.getTotalElements());
        System.out.println("postsPage.getSize() = " + postsPage.getSize());
        System.out.println("postsPage.getTotalPages() = " + postsPage.getTotalPages());
        List<Posts> posts = postsPage.getContent();

        Pageable pageable1 = postsPage.nextPageable();
        Page<Posts> postPage1 = postsRepository.findByTitleContaining("테스트", pageable1);
        List<Posts> posts1 = postPage1.getContent();


        //then
        assertThat(postsPage.getTotalPages()).isEqualTo(2);
        assertThat(posts.size()).isEqualTo(5);
        for (int i = 0; i < posts.size(); i++) {
            assertThat(posts.get(i).getContents()).isEqualTo("테스트 내용" + i);
        }

        assertThat(postPage1.getTotalPages()).isEqualTo(2);
        assertThat(posts1.size()).isEqualTo(5);
        for (int i = 0; i < posts1.size(); i++) {
            assertThat(posts1.get(i).getContents()).isEqualTo("테스트 내용" + (i+posts.size()));
        }





    }
}