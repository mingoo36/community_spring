package org.covy.mingoocommunityspring.post.service;

import lombok.RequiredArgsConstructor;
import org.covy.mingoocommunityspring.post.dto.PostCreateRequestDto;
import org.covy.mingoocommunityspring.post.dto.PostListResponseDto;
import org.covy.mingoocommunityspring.post.dto.PostResponseDto;
import org.covy.mingoocommunityspring.post.dto.PostUpdateRequestDto;
import org.covy.mingoocommunityspring.post.model.Post;
import org.covy.mingoocommunityspring.post.repository.PostRepository;
import org.covy.mingoocommunityspring.user.model.User;
import org.covy.mingoocommunityspring.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회 (페이징)
    public Page<PostListResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostListResponseDto::new);
    }

    // 게시글 검색 (제목 또는 내용)
    public Page<PostListResponseDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleOrContentContaining(keyword, pageable)
                .map(PostListResponseDto::new);
    }

    // 특정 사용자의 게시글 조회
    public Page<PostListResponseDto> getPostsByUserId(Integer userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable)
                .map(PostListResponseDto::new);
    }


    // 게시글 상세 조회 (조회수 증가)
    @Transactional
    public PostResponseDto getPostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        // 조회수 증가
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        
        return new PostResponseDto(post);
    }

    // 게시글 생성
    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto requestDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setImage(requestDto.getImage());
        post.setUser(user);
        post.setViews(0);

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Integer id, PostUpdateRequestDto requestDto, Integer userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        // 수정할 내용이 있을 때만 업데이트
        if (requestDto.getTitle() != null && !requestDto.getTitle().trim().isEmpty()) {
            post.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null && !requestDto.getContent().trim().isEmpty()) {
            post.setContent(requestDto.getContent());
        }
        if (requestDto.getImage() != null) {
            post.setImage(requestDto.getImage());
        }
        
        post.setUpdatedAt(Instant.now());

        Post updatedPost = postRepository.save(post);
        return new PostResponseDto(updatedPost);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Integer id, Integer userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
