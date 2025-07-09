package org.covy.mingoocommunityspring.post.repository;

import org.covy.mingoocommunityspring.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // 특정 사용자의 게시글 조회
    Page<Post> findByUserId(Integer userId, Pageable pageable);
    
    // 제목으로 검색
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // 제목 또는 내용으로 검색
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

}