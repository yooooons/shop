package com.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Entity
public class Posts extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "postId")
    private Long id;

    @Column(length = 500,nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String writer;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<Comments> comments;

    public Posts() {

    }


    /* 게시글 수정 */
    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
