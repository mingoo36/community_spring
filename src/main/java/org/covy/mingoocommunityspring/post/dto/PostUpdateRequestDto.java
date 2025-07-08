package org.covy.mingoocommunityspring.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequestDto {
    
    @Size(max = 255, message = "제목은 255자를 넘을 수 없습니다.")
    private String title;
    
    private String content;
    
    @Size(max = 255, message = "이미지 URL은 255자를 넘을 수 없습니다.")
    private String image;
}
