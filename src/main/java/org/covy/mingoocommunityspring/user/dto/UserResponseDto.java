package org.covy.mingoocommunityspring.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String email;
    private String username;
}
