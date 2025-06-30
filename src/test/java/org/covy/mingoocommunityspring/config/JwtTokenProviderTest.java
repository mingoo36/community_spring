package org.covy.mingoocommunityspring.config;

import org.covy.mingoocommunityspring.config.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expiration;

    @Test
    @DisplayName("토큰 생성 후 사용자 ID를 정상적으로 추출한다")
    void createAndExtractToken() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secret, expiration);
        Integer userId = 123;

        // when
        String token = jwtTokenProvider.createToken(userId);
        Integer extractedUserId = jwtTokenProvider.getUserId(token);
        boolean isValid = jwtTokenProvider.validateToken(token);

        // then
        assertThat(token).isNotNull();
        assertThat(extractedUserId).isEqualTo(userId);
        assertThat(isValid).isTrue();

        System.out.println("token = " + token);
    }

    @Test
    @DisplayName("잘못된 토큰은 유효성 검증에 실패해야 한다")
    void invalidTokenShouldFailValidation() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secret, expiration);
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }
}
