package org.covy.mingoocommunityspring.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String email;
    private String username;
    private String image;

    // 기존 생성자와의 호환성을 위한 오버로드 생성자
    public UserResponseDto(Integer id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.image = null;
    }
}
