package org.covy.mingoocommunityspring.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.covy.mingoocommunityspring.post.model.Post;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PostListResponseDto {
    
    private Integer id;
    private String title;
    private String authorUsername;
    private Instant createdAt;
    private Integer views;

    public PostListResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorUsername = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.views = post.getViews();
    }
}
