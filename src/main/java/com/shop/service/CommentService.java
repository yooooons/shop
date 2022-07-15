package com.shop.service;


import com.shop.dto.CommentsDto;
import com.shop.entity.Comments;
import com.shop.entity.Member;
import com.shop.entity.Posts;
import com.shop.repo.CommentRepository;
import com.shop.repo.MemberRepository;
import com.shop.repo.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostsRepository postsRepository;


    /**
     * CREATE
     */
    @Transactional
    public Long saveComments(Long id, String email, CommentsDto.Request commentsDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않으므로 댓글을 쓰실수 없습니다."));

        commentsDto.setMember(member);
        commentsDto.setPosts(posts);

        Comments comments = commentsDto.toEntity();
        commentRepository.save(comments);


        return comments.getId();
    }




    /**
     * UPDATE
     */
    @Transactional
    public void update(Long id, CommentsDto.Request commentDto) {
        Comments comments = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾으수 없습니다."));
        comments.commentUpdate(commentDto.getComment());
    }


    /**
     * DELETE
     */
    @Transactional
    public void delete(Long id) {
        Comments comments = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾으수 없습니다."));
        commentRepository.delete(comments);
    }
}
