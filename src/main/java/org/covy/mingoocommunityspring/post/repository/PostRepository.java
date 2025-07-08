package org.covy.mingoocommunityspring.post.repository;

import org.covy.mingoocommunityspring.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // 특정 사용자의 게시글 조회
    Page<Post> findByUserId(Integer userId, Pageable pageable);
    
    // 제목으로 검색
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // 제목 또는 내용으로 검색
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Post> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 조회수 상위 N개 게시글
    List<Post> findTop10ByOrderByViewsDesc();
    
    // 최신 게시글 N개
    List<Post> findTop10ByOrderByCreatedAtDesc();
}
