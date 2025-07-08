package org.covy.mingoocommunityspring.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.covy.mingoocommunityspring.post.model.Post;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    
    private Integer id;
    private String title;
    private String content;
    private String image;
    private String authorUsername;
    private Integer authorId;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer views;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.authorUsername = post.getUser().getUsername();
        this.authorId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.views = post.getViews();
    }
}
