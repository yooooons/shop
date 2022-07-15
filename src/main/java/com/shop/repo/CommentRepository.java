package com.shop.repo;

import com.shop.entity.Comments;
import com.shop.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    //게시글 댓글 목록 불러오기
    List<Comments> getCommentsByPostsOrderById(Posts posts);
}
/**
 * getById() 는 해당 엔티티를 사용하기 전까진 DB 에 접근하지 않기 때문에 성능상으로 좀더 유리하다.
 * 따라서 특정 엔티티의 ID 값만 활용할 일이 있다면 DB 에 접근하지 않고 프록시만 가져와서 사용할 수 있다.
 */