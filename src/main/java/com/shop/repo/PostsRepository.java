package com.shop.repo;

import com.shop.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostsRepository extends JpaRepository<Posts, Long> {


    @Modifying(clearAutomatically = true)
    @Query("update Posts p set p.view = p.view + 1 where p.id = :id")
    int updateView(@Param("id") Long id);

    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

}
/**
 * https://velog.io/@roro/JPA-JPQL-update-%EC%BF%BC%EB%A6%AC%EB%B2%8C%ED%81%AC%EC%99%80-%EC%98%81%EC%86%8D%EC%84%B1-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8
 */
