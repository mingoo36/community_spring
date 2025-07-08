package org.covy.mingoocommunityspring.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.covy.mingoocommunityspring.config.auth.CurrentUserId;
import org.covy.mingoocommunityspring.post.dto.PostCreateRequestDto;
import org.covy.mingoocommunityspring.post.dto.PostListResponseDto;
import org.covy.mingoocommunityspring.post.dto.PostResponseDto;
import org.covy.mingoocommunityspring.post.dto.PostUpdateRequestDto;
import org.covy.mingoocommunityspring.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<PostListResponseDto>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<PostListResponseDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<PostListResponseDto>> searchPosts(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<PostListResponseDto> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    // 특정 사용자의 게시글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostListResponseDto>> getPostsByUserId(
            @PathVariable Integer userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<PostListResponseDto> posts = postService.getPostsByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Integer id) {
        PostResponseDto post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostCreateRequestDto requestDto,
            @CurrentUserId Integer userId) {
        
        PostResponseDto createdPost = postService.createPost(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Integer id,
            @Valid @RequestBody PostUpdateRequestDto requestDto,
            @CurrentUserId Integer userId) {
        
        PostResponseDto updatedPost = postService.updatePost(id, requestDto, userId);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(
            @PathVariable Integer id,
            @CurrentUserId Integer userId) {
        
        postService.deletePost(id, userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "게시글이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 전역 예외 처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
