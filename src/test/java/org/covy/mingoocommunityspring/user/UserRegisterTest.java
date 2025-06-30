package org.covy.mingoocommunityspring.user;

import org.covy.mingoocommunityspring.user.dto.UserRegisterDto;
import org.covy.mingoocommunityspring.user.model.User;
import org.covy.mingoocommunityspring.user.repository.UserRepository;
import org.covy.mingoocommunityspring.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRegisterTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback(false)
    @DisplayName("회원가입 성공: 유저가 저장되고 비밀번호는 암호화된다")
    void register_success() {
        // given
        UserRegisterDto dto = new UserRegisterDto();
        dto.setEmail("test@domain.com");
        dto.setPassword("myPassword123!");
        dto.setUsername("이민구");

        // when
        User savedUser = userService.register(dto);

        // then
        assertThat(savedUser.getId()).isNotNull();

        User found = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo(dto.getEmail());
        assertThat(found.getUsername()).isEqualTo(dto.getUsername());

        // 비밀번호는 평문과 달라야 하며
        assertThat(found.getPassword()).isNotEqualTo(dto.getPassword());

        // passwordEncoder로 검증 가능해야 함
        assertThat(passwordEncoder.matches(dto.getPassword(), found.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복 이메일 회원가입 시 IllegalArgumentException 예외 발생")
    void register_duplicateEmail() {
        // given
        String email = "dup@test.com";

        UserRegisterDto dto1 = new UserRegisterDto();
        dto1.setEmail(email);
        dto1.setPassword("pw1");
        dto1.setUsername("사용자1");

        UserRegisterDto dto2 = new UserRegisterDto();
        dto2.setEmail(email);
        dto2.setPassword("pw2");
        dto2.setUsername("사용자2");

        // when
        userService.register(dto1);

        // then
        assertThatThrownBy(() -> userService.register(dto2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 이메일");
    }
}
