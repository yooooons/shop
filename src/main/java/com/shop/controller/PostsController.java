package com.shop.controller;


import com.shop.config.argumentResolver.Login;
import com.shop.dto.CommentsDto;
import com.shop.dto.PostsDto;
import com.shop.entity.Posts;
import com.shop.service.PostsService;
import com.shop.session.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;

    @GetMapping("/posts") //default page = 0, size = 10
    public String index(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @Login SessionMember sessionMember, Model model) {
        Page<PostsDto.Response> list = postsService.pageList(pageable);

        if (sessionMember != null) {
            model.addAttribute("member", sessionMember);
        }

        model.addAttribute("posts", list);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("hasPrev", list.hasPrevious());

        return "/board/postIndex";
    }


    @GetMapping("/posts/write")
    public String write(@Login SessionMember sessionMember, Model model) {
        if (sessionMember != null) {
            model.addAttribute("member", sessionMember);
        }
        return "/board/posts-write";
    }


    @GetMapping("/posts/read/{id}")
    public String read(@PathVariable Long id, @Login SessionMember sessionMember, Model model) {
        PostsDto.Response postDto = postsService.getPost(id);
        List<CommentsDto.Response> comments = postDto.getComments();
        log.info("postId={}", id);

        //댓글
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        //사용자 인증
        if (sessionMember != null) {
            model.addAttribute("member", sessionMember);

            //게시판 작성자와 로그인 사용자 일치 검증
            if (postDto.getMemberId().equals(sessionMember.getId())) {
                model.addAttribute("writer", true);
            }
        }


        postsService.updateView(id);
        model.addAttribute("posts", postDto);
        return "board/posts-read";
    }

    @GetMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, @Login SessionMember sessionMember, Model model) {
        PostsDto.Response postDto = postsService.getPost(id);
        if (sessionMember != null) {
            model.addAttribute("member", sessionMember);
        }
        model.addAttribute("posts", postDto);

        return "board/posts-update";
    }


    @GetMapping("/posts/search")
    public String search(String keyword, Model model,
                         @PageableDefault(sort = "id",direction = Sort.Direction.DESC) Pageable pageable, @Login SessionMember sessionMember) {
        Page<Posts> searchList = postsService.search(keyword, pageable);

        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", searchList.hasNext());
        model.addAttribute("hasPrev", searchList.hasPrevious());

        return "board/posts-search";



    }


}
