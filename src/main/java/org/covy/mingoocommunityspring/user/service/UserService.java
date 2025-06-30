package org.covy.mingoocommunityspring.user.service;

import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.model.User;

import java.util.Optional;

public interface UserService {

    /**
     * 회원가입 수행
     * @param dto 가입에 필요한 정보(email, password, username)
     * @return 저장된 User 엔티티
     */
    User register(UserRegisterDto dto);
    Optional<User> login(String email, String password);

}
