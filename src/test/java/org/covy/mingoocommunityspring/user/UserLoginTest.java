package org.covy.mingoocommunityspring.user;

import jakarta.transaction.Transactional;
import org.covy.mingoocommunityspring.config.jwt.JwtTokenProvider;
import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.model.User;
import org.covy.mingoocommunityspring.user.repository.UserRepository;
import org.covy.mingoocommunityspring.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserLoginTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void clearUserTable() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 후 로그인 시 JWT 토큰이 발급된다")
    void registerThenLoginAndGetJwtToken() {
        // given
        UserRegisterDto dto = new UserRegisterDto();
        dto.setEmail("test@example.com");
        dto.setPassword("1234");
        dto.setUsername("테스트유저10");

        User registeredUser = userService.register(dto);

        // when
        Optional<User> loginResult = userService.login("test@example.com", "1234");

        // then
        assertThat(loginResult).isPresent();

        User loggedInUser = loginResult.get();
        String token = jwtTokenProvider.createToken(loggedInUser.getId());

        System.out.println("✅ 발급된 토큰: " + token);

        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.getUserId(token)).isEqualTo(loggedInUser.getId());
    }
}
