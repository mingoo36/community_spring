package org.covy.mingoocommunityspring.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    
    @Size(min = 2, max = 100, message = "사용자명은 2자 이상 100자 이하여야 합니다.")
    private String username;
    
    private String image;
}
