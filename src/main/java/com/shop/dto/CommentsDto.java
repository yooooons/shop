package com.shop.dto;

import com.shop.entity.Comments;
import com.shop.entity.Member;
import com.shop.entity.Posts;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentsDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {
        private Long id;
        private String comment;
        private String email;
        private Member member;
        private Posts posts;

        public Comments toEntity() {
            Comments comments = Comments.builder()
                    .id(id)
                    .comment(comment)
                    .email(member.getEmail())
                    .member(member)
                    .posts(posts)
                    .build();

            return comments;
        }

    }

    @Getter
    public static class Response {
        private Long id;
        private String comment;
        private String email;
        private String name;
        private Long memberId;
        private Long postsId;
        private LocalDateTime regTime;
        private LocalDateTime updateTime;

        public Response(Comments comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.email = comment.getEmail();
            this.name = comment.getMember().getName();
            this.memberId = comment.getMember().getId();
            this.postsId = comment.getPosts().getId();
            this.regTime = LocalDateTime.parse(comment.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            this.updateTime = LocalDateTime.parse(comment.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        }



    }
}
