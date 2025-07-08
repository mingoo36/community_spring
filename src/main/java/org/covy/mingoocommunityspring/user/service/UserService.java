package org.covy.mingoocommunityspring.user.service;

import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.dto.UserUpdateRequestDto;
import org.covy.mingoocommunityspring.user.model.User;

import java.util.Optional;

public interface UserService {

    /**
     * 회원가입 수행
     * @param dto 가입에 필요한 정보(email, password, username)
     * @return 저장된 User 엔티티
     */
    User register(UserRegisterDto dto);
    
    /**
     * 로그인 수행
     * @param email 이메일
     * @param password 비밀번호
     * @return 로그인 성공 시 User 엔티티
     */
    Optional<User> login(String email, String password);
    
    /**
     * 회원정보 수정
     * @param userId 수정할 사용자 ID
     * @param dto 수정할 정보 (username, image)
     * @return 수정된 User 엔티티
     */
    User updateUser(Integer userId, UserUpdateRequestDto dto);
    
    /**
     * 사용자 정보 조회
     * @param userId 사용자 ID
     * @return User 엔티티
     */
    User getUserById(Integer userId);

}
