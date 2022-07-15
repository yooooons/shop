package com.shop.controller;

import com.shop.config.argumentResolver.Login;
import com.shop.dto.CommentsDto;
import com.shop.service.CommentService;
import com.shop.session.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    /* CREATE */
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity save(@PathVariable Long id, @RequestBody CommentsDto.Request commentDto,
                               @Login SessionMember sessionMember) {
        return ResponseEntity.ok(commentService.saveComments(id, sessionMember.getEmail(), commentDto));
    }


    /* UPDATE */
    @PutMapping("/posts/{id}/comments/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CommentsDto.Request dto) {
        commentService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    /* DELETE */
    @DeleteMapping("/posts/{id}/comments/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        commentService.delete(id);
        return ResponseEntity.ok(id);
    }


}
