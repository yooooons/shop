package com.shop.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "commentId")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;


    @Column(nullable = false)
    private String email;


    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //댓글 수정
    public void commentUpdate(String comment) {
        this.comment = comment;
    }


}
