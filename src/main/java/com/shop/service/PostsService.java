package com.shop.service;

import com.shop.dto.PostsDto;
import com.shop.entity.Member;
import com.shop.entity.Posts;
import com.shop.repo.MemberRepository;
import com.shop.repo.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final MemberRepository memberRepository;


    /**
     * CREATE
     */
    @Transactional
    public Long savePost(PostsDto.Request postsDto, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        postsDto.setMember(member);
        log.info("PostsService savePost실행");
        Posts posts = postsDto.toEntity();
        postsRepository.save(posts);
        return posts.getId();
    }

    /**
     * READ
     */
    @Transactional(readOnly = true)
    public PostsDto.Response getPost(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다. id: " + id));
        return new PostsDto.Response(posts);
    }

    /**
     * UPDATE
     */
    @Transactional
    public void update(Long id, PostsDto.Request postsDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다. id: " + id));
        posts.update(postsDto.getTitle(), postsDto.getContents());
    }


    /**
     * DELETE
     */
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다. id: " + id));
        postsRepository.delete(posts);
    }


    /**
     * Views Counting
     */
    @Transactional
    public int updateView(Long id) {
        return postsRepository.updateView(id);
    }


    /**
     * Paging and Sort
     */
    @Transactional(readOnly = true)
    public Page<PostsDto.Response> pageList(Pageable pageable) {
        return postsRepository.findAll(pageable).map(p -> new PostsDto.Response(p));
    }


    /**
     * Search
     */
    @Transactional(readOnly = true)
    public Page<Posts> search(String keyword, Pageable pageable) {
        Page<Posts> postsList = postsRepository.findByTitleContaining(keyword, pageable);
        return postsList;
    }
}
