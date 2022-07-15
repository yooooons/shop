package com.shop.dto;


import com.shop.entity.Comments;
import com.shop.entity.Member;
import com.shop.entity.Posts;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * request, response DTO 클래스를 하나로 묶어 InnerStaticClass로 한 번에 관리
 */
public class PostsDto {


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {
        private Long id;
        private String title;
        private String writer;
        private String contents;
        private String email;
        private int view;
        private Member member;

        public Posts toEntity() {
            Posts posts = Posts.builder()
                    .id(id)
                    .title(title)
                    .writer(writer)
                    .contents(contents)
                    .email(member.getEmail())
                    .view(0)
                    .member(member)
                    .build();
            return posts;
        }
    }


    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String writer;
        private String contents;
        private String email;
        private LocalDateTime regTime;
        private LocalDateTime updateTime;
        private int view;
        private Long memberId;
        private List<CommentsDto.Response> comments;

        public Response(Posts posts) {
            this.id = posts.getId();
            this.title = posts.getTitle();
            this.writer = posts.getWriter();
            this.contents = posts.getContents();
            this.email = posts.getEmail();
            this.regTime = LocalDateTime.parse(posts.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            this.updateTime = LocalDateTime.parse(posts.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            this.view = posts.getView();
            this.memberId = posts.getMember().getId();
            this.comments = posts.getComments().stream().map(CommentsDto.Response::new).collect(Collectors.toList());
            //https://hbase.tistory.com/78
        }
    }



}
