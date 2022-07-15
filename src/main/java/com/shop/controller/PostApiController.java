package com.shop.controller;

import com.shop.config.argumentResolver.Login;
import com.shop.dto.PostsDto;
import com.shop.service.PostsService;
import com.shop.session.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {
    private final PostsService postsService;

    /* CREATE */
    @PostMapping("posts")
    public ResponseEntity save(@RequestBody PostsDto.Request postDto, @Login SessionMember sessionMember) {
        return ResponseEntity.ok(postsService.savePost(postDto, sessionMember.getEmail()));
    }

    /* READ */
    @GetMapping("/posts/{id}")
    public ResponseEntity read(@PathVariable Long id) {
        return ResponseEntity.ok(postsService.getPost(id));
    }

    /* UPDATE */
    @PutMapping("/posts/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostsDto.Request postDto) {
        postsService.update(id, postDto);
        return ResponseEntity.ok(id);
    }

    /* DELETE */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        postsService.delete(id);
        return ResponseEntity.ok(id);
    }



}
